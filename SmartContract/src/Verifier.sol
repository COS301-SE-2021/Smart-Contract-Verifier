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

    constructor(UnisonToken token, RandomSource randomSource){
        unisonToken = token;
        jurorStore = new JurorStore(address(this), randomSource);
    }

    // If agreements[agreeID] is null, this will also fail since msg.sender will never be 0
    modifier inAgreement(uint agreeID){
        require(msg.sender == agreements[agreeID].party1 || msg.sender == agreements[agreeID].party1);
        _;
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

    function payPlatformFee(uint agreeID) public{
        // Anyone can pay the platform fee, it does not even have to be one of the
        // parties involved in the agreement

        // BUG: If payment is split up over multiple accounts, the last account will receive the entire refund

        require(agreements[agreeID].state == AgreementLib.AgreementState.ACCEPTED);

        uint256 payment = agreements[agreeID].platformFee - agreements[agreeID].feePaid;
        require(payment > 0);

        uint256 allowed = unisonToken.allowance(msg.sender, address(this));
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

    function updateStateAfterVote(uint agreeID) internal{

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

    function voteResolution(uint agreeID, AgreementLib.Vote vote) inAgreement(agreeID) public{
        require(agreements[agreeID].resolutionTime < block.timestamp, "It's too soon to vote");
        require(agreements[agreeID].state == AgreementLib.AgreementState.ACTIVE
            || agreements[agreeID].state == AgreementLib.AgreementState.COMPLETED, "Agreement not in valid state for voting");

        if(msg.sender == agreements[agreeID].party1){
            agreements[agreeID].party1Vote = vote;
            updateStateAfterVote(agreeID);
        }
        else if(msg.sender == agreements[agreeID].party2){
            agreements[agreeID].party2Vote = vote;
            updateStateAfterVote(agreeID);
        }
    }

    // Sign yourself up to become a juror
    function addJuror() public{
        jurorStore.addJuror(msg.sender);
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