// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

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
        if(numJurors == 0)
            numJurors = 1;
        if(numActive == 0)
            numActive = 1;

        uint divider = numJurors * targetRatio;
        // The 1000 * is to cancel out the fact that the divider is a fraction out of 1000
        platformFee *= 1000 * numActive;
        platformFee /= divider;


    }

}