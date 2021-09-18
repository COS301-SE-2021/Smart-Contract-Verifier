// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./UnisonToken.sol";

contract Faucet{
    UnisonToken private token;

    constructor(UnisonToken _token){
        token = _token;
    }

    // Adds token to the pool faucet works from
    function dumpToken(uint256 amount) external{
        uint256 allowed = token.allowance(tx.origin, address(this));
        require(allowed >= amount, "Insufficient allowance");

        token.transferFrom(tx.origin, address(this), amount);
    }

    function getToken() external{
        token.transfer(tx.origin, 1000000000000000000); //1 UNT
    }

}