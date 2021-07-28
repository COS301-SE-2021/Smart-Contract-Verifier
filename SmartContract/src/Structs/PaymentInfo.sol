// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "../Token/IERC20.sol";

library PaymentInfoLib{
    struct PaymentInfo{
        IERC20 token;
        uint256 amount;
    }

    function makePaymentInfo(IERC20 _token, uint256 a) pure internal returns(PaymentInfo memory){
        return PaymentInfo(_token, a);
    }
}