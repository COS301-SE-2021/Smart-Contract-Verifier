// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./UnisonToken.sol";

library AgreementLib{

    // Not all of these states are in use yet and some are likely to change

    enum AgreementState{
        PENDING,  //0
        PROPOSED, //1
        REJECTED, //2
        ACCEPTED, //3
        ACTIVE,   //4
        COMPLETED,//5
        SETTLED,  //6
        CONTESTED,//7
        DECIDED,  //8
        CLOSED    //9
    }

    struct Agreement{
        address party1;
        address party2;
        uint resolutionTime;

        uint256 platformFee;
        uint256 feePaid;
        address feePayer;

        AgreementState state;

        bool party1Vote;
        bool party2Vote;

    }

    // Below are constructors for the Agreement struct

    function makeAgreement() pure internal returns(Agreement memory){
        return Agreement(address(0),address(0),0,0,0, address(0), AgreementState.PROPOSED, false, false);
    }

    function makeAgreement(address party1, address party2, uint resolutionTime) pure internal
            returns(Agreement memory){
        return Agreement(party1, party2, resolutionTime, 0, 0, address(0), AgreementState.PROPOSED, false, false);
    }

    function makeAgreement(address party1, address party2, uint resolutionTime, uint256 platformFee) pure internal
            returns(Agreement memory){
        return Agreement(party1, party2, resolutionTime, platformFee, 0, address(0), AgreementState.PROPOSED, false, false);
    }

}
