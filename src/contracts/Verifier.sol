pragma solidity >=0.4.22 <0.9.0;

pragma experimental ABIEncoderV2;

contract Verifier{

    uint private nextAgreeID = 0;

    struct Agreement{
        address party1;
        address party2;
        bool accepted;
    }

    // Non-existent entries will return a struct filled with 0's
    mapping(uint => Agreement) agreements;

    function createAgreement(address party2) public returns(uint agreementID){
        agreements[nextAgreeID] = Agreement(msg.sender, party2, false);
        nextAgreeID++;
        return nextAgreeID - 1;
    }

    function acceptAgreement(uint agreeID) public{
        // if(agreements[agreeID].party1 == address(0))
        //     return;

        if(msg.sender == agreements[agreeID].party2){
            agreements[agreeID].accepted = true;
        }
    }

    function getAgreement(uint agreeID) public view returns(Agreement memory){
        return agreements[agreeID];
    }


}