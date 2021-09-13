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

    struct Agreement{
        string uuid;
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

    function voteResolution(Agreement storage agreement, AgreementLib.Vote vote) external{
                require(agreement.resolutionTime < block.timestamp, "E7");

        require(agreement.state == AgreementLib.AgreementState.ACTIVE
            || agreement.state == AgreementLib.AgreementState.COMPLETED, "E8");

        uint index = 0;
        // index starts at 1, 0 means not included
        if(agreement.party1 == tx.origin)
            index = 1;
        else if(agreement.party2 == tx.origin)
            index = 2;

        require(index > 0, "E9");

        if(index == 1){
            require(agreement.party1Vote == AgreementLib.Vote.NONE, "E10");
            agreement.party1Vote = vote;
        }
        else{
            require(agreement.party2Vote == AgreementLib.Vote.NONE, "E10");
             agreement.party2Vote = vote;
        }
    }

    function addPayments(Agreement storage agreement, address party2, IERC20[] calldata tokens,
            uint256[] calldata amount, bool[] calldata direction) external{
        require(tokens.length == amount.length, "E3"); //implicitly covers amount = direction
        require(tokens.length == direction.length, "E3");
        require(tokens.length > 0, "E20");

        // Payment conditions
        for(uint i=0; i<tokens.length; i++){
            address to;
            address from;
            if(direction[i]){
                to = party2;
                from = tx.origin;
            }
            else{
                to = tx.origin;
                from = party2;
            }

            agreement.payments[agreement.numPayments].token = tokens[i];
            agreement.payments[agreement.numPayments].amount = amount[i];
            agreement.payments[agreement.numPayments].from = from;
            agreement.payments[agreement.numPayments].to = to;
            agreement.numPayments++;

        }

    }

    function requirePaidIn(Agreement storage agreement) view external{
        for(uint i=0; i<agreement.numPayments; i++){
            require(agreement.payments[i].paidIn, "E19");
        }
    }

    struct Jury{
        bool assigned;
        uint deadline;
        mapping(uint => address) jurors;
        mapping(uint => Vote) votes;
        uint numJurors;

        mapping(uint => string) evidenceFile;
        mapping(uint => uint256) evidenceHash;
        uint numFiles;

    }

    function addEvidence(Jury storage jury, string calldata file, uint256 fileHash) external{
        jury.evidenceFile[jury.numFiles] = file;
        jury.evidenceHash[jury.numFiles] = fileHash;
        jury.numFiles++;
    }

    //return indicates if it's time to make a decision
    function juryVote(Jury storage jury, AgreementLib.Vote vote) external returns(bool){
        // Yes means pay out as normal, no means refund all payments
        require(jury.assigned, "E11");
        require(jury.deadline > block.timestamp, "E18");

        // Get index of juror
        int index = -1;

        for(uint i=0; i<jury.numJurors; i++){
            if(jury.jurors[i] == msg.sender){
                index = int(i);
            }
        }
 
        require(index >= 0, "E12");
        // If the following two conditions hold, then the agreement also can't be closed.
        require(jury.deadline > block.timestamp, "E13");
        require(jury.votes[uint(index)] == AgreementLib.Vote.NONE, "E10");

        // Set vote
        jury.votes[uint(index)] = vote;

        // Determine if it's time to make decision        
        if(jury.deadline > block.timestamp){
            // If deadline hasn't been reached and there are outstanding votes, return
            for(uint i=0; i < jury.numJurors; i++){
                if(jury.votes[i] == AgreementLib.Vote.NONE)
                    return false;
            }
        }
        return true;
    }

    // Version of Agreement to be used in functions as return value
    // Agreement can't be returned by a function since it contains a mapping, but ReturnAgreement can't
    // be in storage since it contains a dynamic array of structs
    struct ReturnAgreement{
        string uuid;
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

    struct ReturnEvidence{
        string[] url;
        uint256[] evidenceHash;
    }

    function makeReturnEvidence(Jury storage jury) external view returns(ReturnEvidence memory){
        ReturnEvidence memory result;
        if(jury.numFiles <= 0)
            return result;
        
        result.url = new string[](jury.numFiles);
        result.evidenceHash = new uint256[](jury.numFiles);
        
        for(uint i=0; i<jury.numFiles; i++){
            result.url[i] = jury.evidenceFile[i];
            result.evidenceHash[i] = jury.evidenceHash[i];
        }

        return result;
    }


    struct ReturnJury{
        bool assigned;
        uint deadline;
        address[] jurors;
        Vote[] votes;
    }

    function makeReturnAgreement(Agreement storage agreement) external view returns(ReturnAgreement memory){
        ReturnAgreement memory result;

        result.uuid = agreement.uuid;

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

    function makeReturnJury(Jury storage jury) external view returns(ReturnJury memory){
        ReturnJury memory result;

        result.assigned = jury.assigned;
        if(!result.assigned)
            return result;

        result.deadline = jury.deadline;
        result.jurors = new address[](jury.numJurors);
        result.votes = new Vote[](jury.numJurors);
        for(uint i=0; i<jury.numJurors; i++){
            result.jurors[i] = jury.jurors[i];
            result.votes[i] = jury.votes[i];
        }
        
        return result;
    }
}
