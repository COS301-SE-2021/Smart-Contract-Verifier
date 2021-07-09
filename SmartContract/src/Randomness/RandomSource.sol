// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

// Separated into own contract to make possible oracle integation easier

contract RandomSource{
    // A pseudorandom value is calculated by taking the XOR of the last two block hashes 
    // XOR should maintain a 50/50 ratio between 0's and 1's, keeping the distribution uniform
    function getRandVal() public view returns(uint256){
        uint256 newBlock = uint256(blockhash(block.number));
        if(block.number-1 > 0){
            uint256 oldBlock = uint256(blockhash(block.number-1));
            newBlock  = newBlock ^ oldBlock;
        }
        return newBlock;
    }
}