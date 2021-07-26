// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

// Separated into own contract to make possible oracle integation easier

contract RandomSource{
    // A pseudorandom value is calculated by taking the XOR of the last two block hashes 
    // XOR should maintain a 50/50 ratio between 0's and 1's, keeping the distribution uniform

    // Only generates one random value per block
    function getRandVal() public view returns(uint256){
        uint256 newBlock = uint256(blockhash(block.number));

        if(block.number-1 > 0){
            // Should be the case every time, the check might be needed on fresh blockchains, though
            uint256 oldBlock = uint256(blockhash(block.number-1));
            newBlock  = newBlock ^ oldBlock;
        }
        return newBlock;
    }

    // Combines seed with block value, so seed is not repeatable in different blocks
    function getRandVal(uint256 seed) public view returns(uint256){
        unchecked{
        uint256 newBlock = uint256(blockhash(block.number));
        if(newBlock == 0){
            // Due to a bug in (probably) truffle, blockhash always returns 0 in tests
            // This piece of code won't ever run if deployed
            newBlock = 0xcb3ac66d0d41caf6ca2b612067fe73de;
        }

        uint256 temp = newBlock >> (256 - (seed & 256));
        newBlock = newBlock << (seed % 256);

        newBlock = newBlock * temp;

        if(block.number-1 > 0){
            uint256 oldBlock = uint256(blockhash(block.number-1));
            if(oldBlock == 0){
                // Due to a bug in (probably) truffle, blockhash always returns 0 in testing
                // This piece of code won't ever run if deployed
                oldBlock = 0x6967777748efec589589d8bf3acaeb44;
            }
            newBlock  = newBlock ^ oldBlock;
        }
        return newBlock;
        }
    }
}