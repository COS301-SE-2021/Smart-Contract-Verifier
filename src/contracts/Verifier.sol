pragma solidity >=0.4.22 <0.9.0;

pragma experimental ABIEncoderV2;

contract Verifier{

    uint private nextAgreeID = 0;

    struct Agreement{
        address party1;
        address party2;
    }

    // Non-existent entries will return a struct filled with 0's
    mapping(uint => Agreement) agreements;

    function createAgreement(address party2) public returns(uint agreementID){
        
        agreements[nextAgreeID] = Agreement(msg.sender, party2);
        nextAgreeID++;
        return nextAgreeID - 1;
    }

    function getAgreement(uint agreeID) public view returns(Agreement memory){
        return agreements[agreeID];
    }


}