// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./UnisonToken.sol";

// pragma experimental ABIEncoderV2;

contract Verifier{

    uint private nextAgreeID = 0;

    struct Agreement{
        address party1;
        address party2;
        uint resolutionTime;
        uint256 platformFee;
        bool accepted;
        bool party1Vote;
        bool party2Vote;
        bool closed;
    }

    // Non-existent entries will return a struct filled with 0's
    mapping(uint => Agreement) agreements;

    UnisonToken unisonToken;

    constructor(UnisonToken token){
        unisonToken = token;
    }

    function createAgreement(address party2, uint resolutionTime) public{
        // A resolution time in the past is allowed and will mean that the agreement can be resolved at an time after its creation
        agreements[nextAgreeID] = Agreement(msg.sender, party2, resolutionTime, 100000000, false, false, false, false);
        emit CreateAgreement(msg.sender, party2, nextAgreeID);
        nextAgreeID++;
    }

    function acceptAgreement(uint agreeID) public{
        // if(agreements[agreeID].party1 == address(0))
        //     return;

        if(msg.sender == agreements[agreeID].party2){
            agreements[agreeID].accepted = true;
            emit AcceptAgreement(agreeID);
        }
    }

    // function payPlatformFee(uint agreeID) public{
    //     // Anyone can pay the platform fee, it does not even have to be one of the
    //     // parties involved in the agreement

        

    // }


    function getAgreement(uint agreeID) public view returns(Agreement memory){
        return agreements[agreeID];
    }

    function voteResolution(uint agreeID, bool vote) public{
        require(agreements[agreeID].resolutionTime < block.timestamp);
        require(agreements[agreeID].accepted);

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
                agreements[agreeID].closed = true;
                emit CloseAgreement(agreeID);
            }
        }
    }

    event CreateAgreement(address party1, address party2, uint agreeID);
    event AcceptAgreement(uint agreeID);
    event CloseAgreement(uint agreeID);

}