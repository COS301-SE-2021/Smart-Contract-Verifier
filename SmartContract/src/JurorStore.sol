// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

// pragma experimental ABIEncoderV2;

contract JurorStore{
    address owner;

    address[] jurors;
    mapping(address => uint256) jurorIndex;

    // Restricts usage of any function with this modifier to the owner (the Verifier contract)
    modifier onlyOwner(){
        require(msg.sender == owner);
        _;
    }

    constructor(address _owner){
        owner = _owner;
    }

    function addJuror(address j) public onlyOwner(){
        uint256 index = jurors.length;
        jurors.push(j);
        jurorIndex[j] = index;
        emit AddJuror(j, index);
    }

    event AddJuror(address j, uint256 index);

}
