// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "../Token/IERC20.sol";

library PaymentInfoLib{
    struct PaymentInfo{
        IERC20 token;
        uint256 amount;
        address from;
        address to;
    }

    // Adding constructors like this makes changes to the struct easier
    function makePaymentInfo(IERC20 _token, uint256 a, address _from, address _to) pure internal returns(PaymentInfo memory){
        PaymentInfo memory result;
        result.token = _token;
        result.amount = a;
        result.from = _from;
        result.to = _to;
        return result;
        // return PaymentInfo(_token, a, _from, _to);
    }
}