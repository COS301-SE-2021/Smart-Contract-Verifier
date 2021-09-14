// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./UnisonToken.sol";
import "./Structs/AgreementLib.sol";
import "./JurorStore.sol";
import "./FeeContract.sol";

// pragma experimental ABIEncoderV2;

contract Verifier{
    using AgreementLib for AgreementLib.Agreement;
    using AgreementLib for AgreementLib.Jury;

    uint private nextAgreeID = 0;
    uint private numActive = 0; //number of active agreements

    // Non-existent entries will return a struct filled with 0's
    mapping(uint => AgreementLib.Agreement) private agreements;
    mapping(uint => AgreementLib.Jury) private juries;

    JurorStore private jurorStore;
    uint private jurySeed = 10;
    UnisonToken private unisonToken;
    FeeContract private feeContract;

    // Fraction out of a thousand
    uint constant controversyRatio = 300;

    constructor(UnisonToken token, RandomSource randomSource){
        unisonToken = token;
        feeContract = new FeeContract(address(this));
        jurorStore = new JurorStore(address(this), randomSource);
    }

    // If agreements[agreeID] is null, this will also fail since tx.origin will never be 0
    modifier inAgreement(uint agreeID){
        require(tx.origin == agreements[agreeID].party1 || tx.origin == agreements[agreeID].party2);
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

    function createAgreement(address party2, uint resolutionTime, string calldata text, string memory uuid,
             IERC20[] calldata tokens, uint256[] calldata amount, bool[] calldata direction) public{
        // A resolution time in the past is allowed and will mean that the agreement can be resolved at an time after its creation
        // direction is true if from party1 to party2, false otherwise

        // General agreement terms
        agreements[nextAgreeID].party1 = tx.origin;
        agreements[nextAgreeID].party2 = party2;
        agreements[nextAgreeID].resolutionTime = resolutionTime;
        agreements[nextAgreeID].text = text;
        agreements[nextAgreeID].state = AgreementLib.AgreementState.PROPOSED;
        agreements[nextAgreeID].platformFee = getPlatformFee();
        agreements[nextAgreeID].uuid = uuid;


        AgreementLib.addPayments(agreements[nextAgreeID], party2, tokens, amount, direction);

        emit CreateAgreement(tx.origin, party2, nextAgreeID, uuid);
        nextAgreeID++;
    }

    function acceptAgreement(uint agreeID) public{
        // if(agreements[agreeID].party1 == address(0))
        //     return;
        require(agreements[agreeID].state == AgreementLib.AgreementState.PROPOSED);
        require(tx.origin == agreements[agreeID].party2, "E16");

        AgreementLib.requirePaidIn(agreements[agreeID]);

        agreements[agreeID].state = AgreementLib.AgreementState.ACCEPTED;
        emit AcceptAgreement(agreeID);
    }

    function rejectAgreement(uint agreeID) public inAgreement(agreeID){
        require(agreements[agreeID].state == AgreementLib.AgreementState.PROPOSED, "E4");

        for(uint i=0; i<agreements[agreeID].numPayments; i++){
            // Doesn't use _rejectAgreement, since that function assumes paidIn == true
            if(agreements[agreeID].payments[i].paidIn){
                PaymentInfoLib.PaymentInfo memory payment = agreements[agreeID].payments[i];
                payment.token.transfer(payment.from, payment.amount);
            }
        }

        agreements[agreeID].state = AgreementLib.AgreementState.REJECTED;
    }

    // Call this method to pay all of your payment conditions, you must set the appropriate allowances before calling it
    function payIn(uint agreeID) external{
        require(agreements[agreeID].state == AgreementLib.AgreementState.PROPOSED, "E4");

        for(uint i=0; i<agreements[agreeID].numPayments; i++){
            if(agreements[agreeID].payments[i].from != tx.origin)
                continue;

            // Check allowance and then call transferFrom
            uint256 allowed = agreements[agreeID].payments[i].token.allowance(tx.origin, address(this));
            require(allowed >= agreements[agreeID].payments[i].amount, "E5");
            agreements[agreeID].payments[i].token.transferFrom(tx.origin, address(this), agreements[agreeID].payments[i].amount);
            agreements[agreeID].payments[i].paidIn = true;            
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

        uint256 allowed = unisonToken.allowance(tx.origin, address(this));
        require(allowed >= payment, "E5");

        if(unisonToken.transferFrom(tx.origin, address(this), payment)){
            agreements[agreeID].feePaid += payment;
            agreements[agreeID].feePayer = tx.origin;
            if(agreements[agreeID].feePaid == agreements[agreeID].platformFee){
                agreements[agreeID].state = AgreementLib.AgreementState.ACTIVE;
                numActive++;
                emit ActiveAgreement(agreeID);
            }
        }
        else{
            require(false, "E17");
        }

        feeContract.updatePlatformFee(numActive, jurorStore.getNumJurors());
    }


    function getAgreement(uint agreeID) public view returns(AgreementLib.ReturnAgreement memory){
        return AgreementLib.makeReturnAgreement(agreements[agreeID]);
    }

    function getJury(uint agreeID) public view returns(AgreementLib.ReturnJury memory){
        return AgreementLib.makeReturnJury(juries[agreeID]);
    }

    function getEvidence(uint agreeID) external view returns(AgreementLib.ReturnEvidence memory){
        return AgreementLib.makeReturnEvidence(juries[agreeID]);
    }

    function addEvidence(uint agreeID, string calldata url, uint256 evidenceHash) public inAgreement(agreeID){
        require(agreements[agreeID].state == AgreementLib.AgreementState.CONTESTED, "E6");
        AgreementLib.addEvidence(juries[agreeID], url, evidenceHash);
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
        agreements[agreeID].state = AgreementLib.AgreementState.CONTESTED;
        emit JuryAssigned(agreeID, jury);
    }

    function voteResolution(uint agreeID, AgreementLib.Vote vote) public{
        // Cannot be called if other party already voted no
        AgreementLib.voteResolution(agreements[agreeID], vote);

        // update state after vote
        if(vote == AgreementLib.Vote.NO){
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

    // Sign yourself up to become a juror
    // You have to approve an allowance for the staked coins
    function addJuror() public{
        address j = tx.origin;

        uint allowed = unisonToken.allowance(j, address(this));
        require(allowed >= getStakingAmount());
        unisonToken.transferFrom(j, address(this), getStakingAmount());

        jurorStore.addJuror(j);
        emit AddJuror(j);

        feeContract.updatePlatformFee(numActive, jurorStore.getNumJurors());
    }

    // remove yourself from available jurors list
    function removeJuror() public{
        jurorStore.removeJuror(tx.origin);
        unisonToken.transfer(tx.origin, getStakingAmount());
        emit RemoveJuror(tx.origin);

        feeContract.updatePlatformFee(numActive, jurorStore.getNumJurors());
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
            payPerJuror = agreements[agreeID].feePaid / no;
            controversy = (1000 * yes) /(no + yes);
        }
        else{
            // Jury voted yes (even result is counted as yes), pay out as normal
            decision = AgreementLib.Vote.YES;
            _payoutAgreement(agreeID);
            if(yes > 0)
                payPerJuror = agreements[agreeID].feePaid / yes;
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
        if(AgreementLib.juryVote(juries[agreeID], vote))
            _juryMakeDecision(agreeID);

    }

    function triggerPayout(uint agreeID) public{
        // If not all jury members voted, this will be needed to finish the agreement
        // (since code execution must come from someone)

        require(juries[agreeID].assigned, "E11");
        require(juries[agreeID].deadline <= block.timestamp, "E14");
        require(agreements[agreeID].state != AgreementLib.AgreementState.CLOSED, "E15");

        _juryMakeDecision(agreeID);

    }

    function getPlatformFee() public view returns(uint){
        return feeContract.getPlatformFee();
    }

    function getStakingAmount() public view returns(uint){
        return feeContract.getStakingAmount();
    }

    event CreateAgreement(address party1, address party2, uint agreeID, string uuid);
    event AcceptAgreement(uint agreeID);
    event ActiveAgreement(uint agreeID);
    event CloseAgreement(uint agreeID);

    event JuryAssigned(uint agreeID, address[] jury);

    event AddJuror(address juror);
    event RemoveJuror(address juror);

}