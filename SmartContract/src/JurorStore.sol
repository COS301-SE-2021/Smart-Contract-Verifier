// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

// pragma experimental ABIEncoderV2;

contract JurorStore{
    address owner;

    bool tempVal = false;

    // Restricts usage of any function with this modifier to the owner (the Verifier contract)
    modifier onlyOwner(){
        require(msg.sender == owner);
        _;
    }

    constructor(address _owner){
        owner = _owner;
    }

    function addJuror(address j) public onlyOwner(){
        tempVal = true;
    }

    function getTempVal() public view returns(bool){
        return tempVal;
    }


}