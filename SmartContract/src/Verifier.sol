// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./UnisonToken.sol";
import "./AgreementLib.sol";

// pragma experimental ABIEncoderV2;

contract Verifier{
    using AgreementLib for AgreementLib.Agreement;

    uint private nextAgreeID = 0;

    // Non-existent entries will return a struct filled with 0's
    mapping(uint => AgreementLib.Agreement) agreements;

    UnisonToken unisonToken;

    constructor(UnisonToken token){
        unisonToken = token;
    }

    function createAgreement(address party2, uint resolutionTime) public{
        // A resolution time in the past is allowed and will mean that the agreement can be resolved at an time after its creation

        agreements[nextAgreeID] = AgreementLib.makeAgreement(msg.sender, party2, resolutionTime, 1000000000);

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
        uint256 payment = agreements[agreeID].platformFee - agreements[agreeID].feePaid;
        require(payment > 0);

        uint256 allowed = unisonToken.allowance(msg.sender, address(this));
        if(allowed < payment)
            payment = allowed;


        if(unisonToken.transferFrom(msg.sender, address(this), payment)){
            agreements[agreeID].feePaid += payment;
        }

    }


    function getAgreement(uint agreeID) public view returns(AgreementLib.Agreement memory){
        return agreements[agreeID];
    }

    function voteResolution(uint agreeID, bool vote) public{
        require(agreements[agreeID].resolutionTime < block.timestamp);
        require(agreements[agreeID].state == AgreementLib.AgreementState.ACCEPTED);

        if(msg.sender == agreements[agreeID].party1){
            agreements[agreeID].party1Vote = vote;
        }
        else if(msg.sender == agreements[agreeID].party2){
            agreements[agreeID].party2Vote = vote;
        }
    }

    function closeAgreement(uint agreeID) public{
        if(msg.sender == agreements[agreeID].party1
                || msg.sender == agreements[agreeID].party2){
            if(agreements[agreeID].party1Vote == true && agreements[agreeID].party2Vote == true){
                agreements[agreeID].state = AgreementLib.AgreementState.CLOSED;
                emit CloseAgreement(agreeID);
            }
        }
    }

    event CreateAgreement(address party1, address party2, uint agreeID);
    event AcceptAgreement(uint agreeID);
    event CloseAgreement(uint agreeID);

}