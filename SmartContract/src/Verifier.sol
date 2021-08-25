// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./UnisonToken.sol";
import "./Structs/AgreementLib.sol";
import "./JurorStore.sol";


// pragma experimental ABIEncoderV2;

contract Verifier{
    using AgreementLib for AgreementLib.Agreement;

    uint private nextAgreeID = 0;
    uint numActive = 0;

    // Non-existent entries will return a struct filled with 0's
    mapping(uint => AgreementLib.Agreement) agreements;
    mapping(uint => AgreementLib.Jury) juries;

    JurorStore jurorStore;
    uint jurySeed = 10;
    UnisonToken unisonToken;

    uint stakingAmount = 10000;

    // Fraction out of a thousand
    uint constant controversyRatio = 300;

    constructor(UnisonToken token, RandomSource randomSource){
        unisonToken = token;
        jurorStore = new JurorStore(address(this), randomSource);
    }

    // If agreements[agreeID] is null, this will also fail since msg.sender will never be 0
    modifier inAgreement(uint agreeID){
        require(msg.sender == agreements[agreeID].party1 || msg.sender == agreements[agreeID].party2);
        _;
    }

    modifier inJury(uint agreeID){
        require(juries[agreeID].assigned, "Specified agreement has no jury");
        for(uint i=0; i<juries[agreeID].numJurors; i++){
            if(juries[agreeID].jurors[i] == msg.sender)
                return;
        }
        require(false, "You are not on this jury");
        _;
    }

    function _addPaymentToAgreement(uint256 agreeID, PaymentInfoLib.PaymentInfo memory payment) internal{
        agreements[agreeID].payments[agreements[agreeID].numPayments] = payment;
        agreements[agreeID].numPayments++;
    }

    function _payoutAgreement(uint agreeID) internal{
        for(uint i=0; i<agreements[agreeID].numPayments; i++){
            PaymentInfoLib.PaymentInfo memory payment = agreements[agreeID].payments[i];
            payment.token.transfer(payment.to, payment.amount);
        }
    }

    function _refundAgreement(uint agreeID) internal{
        for(uint i=0; i<agreements[agreeID].numPayments; i++){
            PaymentInfoLib.PaymentInfo memory payment = agreements[agreeID].payments[i];
            payment.token.transfer(payment.from, payment.amount);
        }
    }

    function createAgreement(address party2, uint resolutionTime, string calldata text, string memory uuid) public{
        // A resolution time in the past is allowed and will mean that the agreement can be resolved at an time after its creation

        agreements[nextAgreeID].party1 = msg.sender;
        agreements[nextAgreeID].party2 = party2;
        agreements[nextAgreeID].resolutionTime = resolutionTime;
        agreements[nextAgreeID].text = text;
        agreements[nextAgreeID].state = AgreementLib.AgreementState.PROPOSED;
        agreements[nextAgreeID].platformFee = 1000000000;
        agreements[nextAgreeID].uuid = uuid;

        emit CreateAgreement(msg.sender, party2, nextAgreeID, uuid);
        nextAgreeID++;
    }

    function acceptAgreement(uint agreeID) public{
        // if(agreements[agreeID].party1 == address(0))
        //     return;
        require(agreements[agreeID].state == AgreementLib.AgreementState.PROPOSED);

        if(msg.sender == agreements[agreeID].party2){
            agreements[agreeID].state = AgreementLib.AgreementState.ACCEPTED;
            emit AcceptAgreement(agreeID);
        }
    }

    // Each payment must have the allowance ready, it will be transferred immediately
    function addPaymentConditions(uint agreeID, IERC20[] calldata tokens, uint256[] calldata amount) inAgreement(agreeID) public{
        require(tokens.length == amount.length, "mismatch between tokens and amounts");
        require(agreements[agreeID].state == AgreementLib.AgreementState.PROPOSED, "Agreement state invalid for adding payments");
        uint numPayments = tokens.length;

        address otherParty;
        if(msg.sender == agreements[agreeID].party1)
            otherParty = agreements[agreeID].party2;
        else
            otherParty = agreements[agreeID].party1;

        for(uint i=0; i<numPayments; i++){
            uint256 allowed = tokens[i].allowance(msg.sender, address(this));
            require(allowed >= amount[i], "Insufficient allowance on a specified payment");

            PaymentInfoLib.PaymentInfo memory p;
            p.token = tokens[i];
            p.from = msg.sender;
            p.to = otherParty;
            p.amount = amount[i];

            tokens[i].transferFrom(msg.sender, address(this), amount[i]);
            _addPaymentToAgreement(agreeID, p);

        }        


    }

    function payPlatformFee(uint agreeID) public{
        // Anyone can pay the platform fee, it does not even have to be one of the
        // parties involved in the agreement

        // Was first intended to be payable by multiple people, but for now it can only be paid by one person.
        // Front-end doesn't use split platform fees yet, but fixing a bug it had would require an api-change

        require(agreements[agreeID].state == AgreementLib.AgreementState.ACCEPTED);

        uint256 payment = agreements[agreeID].platformFee - agreements[agreeID].feePaid;
        require(payment > 0);

        uint256 allowed = unisonToken.allowance(msg.sender, address(this));
        require(allowed >= payment, "insufficient allowance to pay platform fee");

        if(unisonToken.transferFrom(msg.sender, address(this), payment)){
            agreements[agreeID].feePaid += payment;
            agreements[agreeID].feePayer = msg.sender;
            if(agreements[agreeID].feePaid == agreements[agreeID].platformFee){
                agreements[agreeID].state = AgreementLib.AgreementState.ACTIVE;
                numActive++;
                emit ActiveAgreement(agreeID);
            }
        }
    }


    function getAgreement(uint agreeID) public view returns(AgreementLib.ReturnAgreement memory){
        return AgreementLib.makeReturnAgreement(agreements[agreeID]);
    }

    function getJury(uint agreeID) public view returns(AgreementLib.ReturnJury memory){
        return AgreementLib.makeReturnJury(juries[agreeID]);
    }

    function _assignJury(uint agreeID) internal{
        address[] memory noUse = new address[](2);
        noUse[0] = agreements[agreeID].party1;
        noUse[1] = agreements[agreeID].party2;


        address[] memory jury = jurorStore.assignJury(5, jurySeed, noUse);
        jurySeed += 0xAA;

        for(uint i=0; i<jury.length; i++){
            juries[agreeID].jurors[i] = jury[i];
        }
        // Deadline is 10 minutes from now
        juries[agreeID].deadline = block.timestamp + 60000;

        juries[agreeID].numJurors = jury.length;

        juries[agreeID].assigned = true;
        emit JuryAssigned(agreeID, jury);
    }

    function _updateStateAfterVote(uint agreeID) internal{

        if(agreements[agreeID].party1Vote == AgreementLib.Vote.NO ||
                agreements[agreeID].party2Vote == AgreementLib.Vote.NO){
            if(juries[agreeID].assigned)
                return; //Already has jury

            // If at least one party voted no, agreement becomes contested
            _assignJury(agreeID);
        }
        else if(agreements[agreeID].party1Vote == AgreementLib.Vote.YES && 
                agreements[agreeID].party2Vote == AgreementLib.Vote.YES){
            // Both parties voted yes

            // Refund platform fee
            unisonToken.transfer(agreements[agreeID].feePayer, agreements[agreeID].feePaid);

            // Pay out all payment conditions
            _payoutAgreement(agreeID);

            // Close the agreement
            agreements[agreeID].state = AgreementLib.AgreementState.CLOSED;
            numActive--;
            emit CloseAgreement(agreeID);
        }

    }

    function _partyIndex(uint agreeID, address a) internal view returns(uint){
        // index starts at 1, 0 means not included
        if(agreements[agreeID].party1 == a)
            return 1;
        if(agreements[agreeID].party2 == a)
            return 2;
        return 0;
    }

    function voteResolution(uint agreeID, AgreementLib.Vote vote) public{
        require(agreements[agreeID].resolutionTime < block.timestamp, "It's too soon to vote");

        require(agreements[agreeID].state == AgreementLib.AgreementState.ACTIVE
            || agreements[agreeID].state == AgreementLib.AgreementState.COMPLETED, "Agreement not in valid state for voting");

        uint index = _partyIndex(agreeID, msg.sender);
        require(index > 0, "You can only vote if you're part of the agreement");

        if(index == 1){
            require(agreements[agreeID].party1Vote == AgreementLib.Vote.NONE, "You can't vote twice");
            agreements[agreeID].party1Vote = vote;
            _updateStateAfterVote(agreeID);
        }
        else{
            require(agreements[agreeID].party2Vote == AgreementLib.Vote.NONE, "You can't vote twice");
            agreements[agreeID].party2Vote = vote;
            _updateStateAfterVote(agreeID);
        }
    }

    // Sign yourself up to become a juror
    // You have to approve an allowance for the staked coins
    function addJuror() public{
        address j = msg.sender;

        uint allowed = unisonToken.allowance(j, address(this));
        require(allowed >= stakingAmount);
        unisonToken.transferFrom(j, address(this), stakingAmount);

        jurorStore.addJuror(j);
        emit AddJuror(j);
    }

    // remove yourself from available jurors list
    function removeJuror() public{
        jurorStore.removeJuror(msg.sender);
        unisonToken.transfer(msg.sender, stakingAmount);
        emit RemoveJuror(msg.sender);
    }

    function _jurorIndex(uint agreeID) internal view returns(int){
        if(!juries[agreeID].assigned)
            return -1;

        for(uint i=0; i<juries[agreeID].numJurors; i++){
            if(juries[agreeID].jurors[i] == msg.sender)
                return int(i);
        }
        return -1;
    }

    function _decisionTime(uint agreeID) internal view returns(bool){
        // Returns true if it's time to take action on jury's decision
        // Either when voting deadline has been reached or if all jurors voted

        if(!juries[agreeID].assigned)
            return false;
        
        // True if deadline reached
        if(juries[agreeID].deadline <= block.timestamp)
            return true;

        // False if anyone hasn't voted yet
        for(uint i=0; i < juries[agreeID].numJurors; i++){
            if(juries[agreeID].votes[i] == AgreementLib.Vote.NONE)
                return false;
        }

        return true;
    }

    function _abs(int x) internal pure returns (int) {
        return x >= 0 ? x : -x;
    }

    function _juryMakeDecision(uint agreeID) internal{
        // Time to tally up votes & make a decision
        uint yes = 0;
        uint no =0;

        for(uint i=0; i < juries[agreeID].numJurors; i++){
            AgreementLib.Vote v = juries[agreeID].votes[i];
            if(v == AgreementLib.Vote.NO){
                no++;
            }
            else if(v == AgreementLib.Vote.YES){
                yes++;
            }
        }

        AgreementLib.Vote decision;
        uint payPerJuror;
        uint controversy;

        if(no > yes){
            // Jury voted no, do a refund
            decision = AgreementLib.Vote.NO;
            _refundAgreement(agreeID);
            payPerJuror = stakingAmount / no;
            controversy = (1000 * yes) /(no + yes);
        }
        else{
            // Jury voted yes (even result is counted as yes), pay out as normal
            decision = AgreementLib.Vote.YES;
            _payoutAgreement(agreeID);
            payPerJuror = stakingAmount / yes;
            controversy = (1000 * no) /(no + yes);
        }

        // Pay the jurors who voted correctly
        for(uint i=0; i<juries[agreeID].numJurors; i++){
            if(juries[agreeID].votes[i] == decision){
                unisonToken.transfer(juries[agreeID].jurors[i], payPerJuror);
            }
        }

        // Currently, abstaining is not punished. You only miss out on your payment if you abstain

        // Punish any malicious jurors
        if(controversy != 0 && controversy <= controversyRatio){
            // controversy used to avoid punishing jurors on difficult cases
            // controversy of 0 means the decision was unanimous and this step isn't needed
            AgreementLib.Vote wrongVote;
            if(decision == AgreementLib.Vote.YES)
                wrongVote = AgreementLib.Vote.NO;
            else
                wrongVote =  AgreementLib.Vote.YES;

            for(uint i=0; i<juries[agreeID].numJurors; i++){
                if(juries[agreeID].votes[i] == wrongVote)
                    jurorStore.addStrike(juries[agreeID].jurors[i]);

            }
        }


        agreements[agreeID].state = AgreementLib.AgreementState.CLOSED;
        numActive--;
        emit CloseAgreement(agreeID);
    }

    function isJuror(address a) public view returns(bool){
        return jurorStore.isJuror(a);
    }

    function getStrikes(address a) public view returns(uint){
        return jurorStore.getStrikes(a);
    }

    function jurorVote(uint agreeID, AgreementLib.Vote vote) public{
        // Yes means pay out as normal, no means refund all payments

        require(juries[agreeID].assigned, "There is no jury for this agreement");

        int index = _jurorIndex(agreeID);

        require(index >= 0, "You are not on this jury");
        // If the following two conditions hold, then the agreement also can't be closed.
        require(juries[agreeID].deadline > block.timestamp, "Deadline for voting has passed");
        require(juries[agreeID].votes[uint(index)] == AgreementLib.Vote.NONE, "You already voted");

        // Set vote
        juries[agreeID].votes[uint(index)] = vote;

        if(_decisionTime(agreeID)){
            _juryMakeDecision(agreeID);
        }

    }

    function triggerPayout(uint agreeID) public{
        // If not all jury members voted, this will be needed to finish the agreement
        // (since code execution must come from someone)

        require(juries[agreeID].assigned, "There is no jury for this agreement");
        require(juries[agreeID].deadline <= block.timestamp, "Jurors still have time to vote");
        require(agreements[agreeID].state != AgreementLib.AgreementState.CLOSED, "Agreement is already paid out");

        _juryMakeDecision(agreeID);

    }

    function _totalRatio() internal view returns(uint){
        return (1000*numActive) / jurorStore.getNumJurors();
    }

    event CreateAgreement(address party1, address party2, uint agreeID, string uuid);
    event AcceptAgreement(uint agreeID);
    event ActiveAgreement(uint agreeID);
    event CloseAgreement(uint agreeID);

    event JuryAssigned(uint agreeID, address[] jury);

    event AddJuror(address juror);
    event RemoveJuror(address juror);

}