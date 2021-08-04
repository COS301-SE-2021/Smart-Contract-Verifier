// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./UnisonToken.sol";
import "./Structs/AgreementLib.sol";
import "./JurorStore.sol";


// pragma experimental ABIEncoderV2;

contract Verifier{
    using AgreementLib for AgreementLib.Agreement;

    uint private nextAgreeID = 0;

    // Non-existent entries will return a struct filled with 0's
    mapping(uint => AgreementLib.Agreement) agreements;
    mapping(uint => address[]) juries;

    JurorStore jurorStore;
    uint jurySeed = 10;
    UnisonToken unisonToken;

    uint stakingAmount = 10000;

    constructor(UnisonToken token, RandomSource randomSource){
        unisonToken = token;
        jurorStore = new JurorStore(address(this), randomSource, token);
    }

    // If agreements[agreeID] is null, this will also fail since msg.sender will never be 0
    modifier inAgreement(uint agreeID){
        require(msg.sender == agreements[agreeID].party1 || msg.sender == agreements[agreeID].party2);
        _;
    }

    function _addPaymentToAgreement(uint256 agreeID, PaymentInfoLib.PaymentInfo memory payment) internal{
        agreements[agreeID].payments[agreements[agreeID].numPayments] = payment;
        agreements[agreeID].numPayments++;
    }

    function createAgreement(address party2, uint resolutionTime, string calldata text) public{
        // A resolution time in the past is allowed and will mean that the agreement can be resolved at an time after its creation

        agreements[nextAgreeID].party1 = msg.sender;
        agreements[nextAgreeID].party2 = party2;
        agreements[nextAgreeID].resolutionTime = resolutionTime;
        agreements[nextAgreeID].text = text;
        agreements[nextAgreeID].state = AgreementLib.AgreementState.PROPOSED;
        agreements[nextAgreeID].platformFee = 1000000000;

        emit CreateAgreement(msg.sender, party2, nextAgreeID);
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
        uint numPayments = tokens.length;

        address otherParty;
        if(msg.sender == agreements[agreeID].party1)
            otherParty = agreements[agreeID].party2;
        else
            agreements[agreeID].party2;

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

        // BUG: If payment is split up over multiple accounts, the last account will receive the entire refund

        require(agreements[agreeID].state == AgreementLib.AgreementState.ACCEPTED);

        uint256 payment = agreements[agreeID].platformFee - agreements[agreeID].feePaid;
        require(payment > 0);

        uint256 allowed = unisonToken.allowance(msg.sender, address(this));
        require(allowed >= payment, "insufficient allowance to pay platform fee");

        if(allowed < payment)
            payment = allowed;


        if(unisonToken.transferFrom(msg.sender, address(this), payment)){
            agreements[agreeID].feePaid += payment;
            agreements[agreeID].feePayer = msg.sender;
            if(agreements[agreeID].feePaid == agreements[agreeID].platformFee){
                agreements[agreeID].state = AgreementLib.AgreementState.ACTIVE;
                emit ActiveAgreement(agreeID);
            }
        }
    }


    function getAgreement(uint agreeID) public view returns(AgreementLib.ReturnAgreement memory){
        return AgreementLib.makeReturnAgreement(agreements[agreeID]);
    }

    function checkVotes(uint agreeID) internal{
        // Checks if both votes are in

        require(agreements[agreeID].state == AgreementLib.AgreementState.ACTIVE
            || agreements[agreeID].state == AgreementLib.AgreementState.COMPLETED);

        if(agreements[agreeID].party1Vote == AgreementLib.Vote.YES 
                && agreements[agreeID].party2Vote == AgreementLib.Vote.YES){
            unisonToken.transfer(agreements[agreeID].feePayer, agreements[agreeID].feePaid);
            agreements[agreeID].state = AgreementLib.AgreementState.CLOSED;
            emit CloseAgreement(agreeID);
        }
    }

    function _updateStateAfterVote(uint agreeID) internal{

        if(agreements[agreeID].party1Vote == AgreementLib.Vote.NO ||
                agreements[agreeID].party2Vote == AgreementLib.Vote.NO){
            if(agreements[agreeID].hasJury)
                return; //Already has jury

            // If at least one party voted no, agreement becomes contested
            address[] memory noUse = new address[](2);
            noUse[0] = agreements[agreeID].party1;
            noUse[1] = agreements[agreeID].party2;


            address[] memory jury = jurorStore.assignJury(5, jurySeed, noUse);
            jurySeed += 0xAA;
            juries[agreeID] = jury;
            agreements[agreeID].hasJury = true;
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
        // require(agreements[agreeID].resolutionTime < block.timestamp, "It's too soon to vote");
        // require(agreements[agreeID].state == AgreementLib.AgreementState.ACTIVE
        //     || agreements[agreeID].state == AgreementLib.AgreementState.COMPLETED, "Agreement not in valid state for voting");

        uint index = _partyIndex(agreeID, msg.sender);
        // require(index > 0, "You can only vote if you're part of the agreement");

        if(index == 1){
            // require(agreements[agreeID].party1Vote == AgreementLib.Vote.NONE, "You can't vote twice");
            agreements[agreeID].party1Vote = vote;
            _updateStateAfterVote(agreeID);
        }
        else{
            // require(agreements[agreeID].party2Vote == AgreementLib.Vote.NONE, "You can't vote twice");
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
    }

    // remove yourself from available jurors list
    function removeJuror() public{
        jurorStore.removeJuror(msg.sender);
    }

    event CreateAgreement(address party1, address party2, uint agreeID);
    event AcceptAgreement(uint agreeID);
    event ActiveAgreement(uint agreeID);
    event CloseAgreement(uint agreeID);

}