// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

// pragma experimental ABIEncoderV2;

contract FeeContract{
    address owner;

    modifier onlyOwner(){
        require(msg.sender == owner);
        _;
    }

    constructor(address _owner){
        owner = _owner;
    }


}