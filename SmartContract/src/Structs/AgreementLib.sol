// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "../UnisonToken.sol";
import "./PaymentInfo.sol";

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

    enum Vote{
        NONE, // 0
        NO,   // 1
        YES   // 2
    }

    struct Jury{
        bool assigned;
        mapping(uint => address) jurors;
        mapping(uint => Vote) votes;
        uint numJurors;
    }

    struct Agreement{
        address party1;
        address party2;
        uint resolutionTime;
        string text;

        uint256 platformFee;
        uint256 feePaid;
        address feePayer;

        AgreementState state;

        Vote party1Vote;
        Vote party2Vote;

        mapping(uint => PaymentInfoLib.PaymentInfo) payments;
        uint numPayments;
    }

    // Version of Agreement to be used in functions as return value
    // Agreement can't be returned by a function since it contains a mapping, but ReturnAgreement can't
    // be in storage since it contains a dynamic array of structs
    struct ReturnAgreement{
        address party1;
        address party2;
        uint resolutionTime;
        string text;

        uint256 platformFee;
        uint256 feePaid;
        address feePayer;

        AgreementState state;

        Vote party1Vote;
        Vote party2Vote;

        PaymentInfoLib.PaymentInfo[] payments;
    }

    function makeReturnAgreement(Agreement storage agreement) view internal returns(ReturnAgreement memory){
        ReturnAgreement memory result;

        result.party1 = agreement.party1;
        result.party2 = agreement.party2;
        result.resolutionTime = agreement.resolutionTime;
        result.text = agreement.text;

        result.platformFee = agreement.platformFee;
        result.feePaid = agreement.feePaid;
        result.feePayer = agreement.feePayer;

        result.state = agreement.state;

        result.party1Vote = agreement.party1Vote;
        result.party2Vote = agreement.party2Vote;

        result.payments = new PaymentInfoLib.PaymentInfo[](agreement.numPayments);
        for(uint i=0; i<agreement.numPayments; i++){
            result.payments[i] = agreement.payments[i];
        }

        return result;
    }
}
