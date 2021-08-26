// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

// pragma experimental ABIEncoderV2;

contract FeeContract{
    address owner;

    uint stakingAmount = 10000;

    uint platformFee = 1000000000;
    uint constant targetRatio = 5000; //ratio of agreements to jurors multiplied by 1000

    modifier onlyOwner(){
        require(msg.sender == owner);
        _;
    }

    constructor(address _owner){
        owner = _owner;
    }

    function getPlatformFee() public view returns(uint){
        return platformFee;
    }

    function getStakingAmount() public view returns(uint){
        return stakingAmount;
    }

    function updatePlatformFee(uint numActive, uint numJurors) public onlyOwner(){

        // multiplied by 1000 an extra time so that the 1000's don't cancel out

        uint totalRatio = (1000 * 1000 * numActive) / numJurors;

        uint error = totalRatio / targetRatio;

        // error is now a fraction where 1000 represents 1, so platform fee must be divided by 1000
        // to correct for that
        platformFee *= error;
        platformFee /= 1000;

    }

}