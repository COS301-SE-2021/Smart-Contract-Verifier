// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./UnisonToken.sol";

library AgreementLib{

    struct Agreement{
        address party1;
        address party2;
        uint resolutionTime;

        uint256 platformFee;
        uint256 feePaid;

        bool accepted;
        bool party1Vote;
        bool party2Vote;
        bool closed;
    }

    function makeEmptyAgreement() pure internal returns(Agreement memory){
        return Agreement(address(0),address(0),0,0,0,false, false, false, false);
    }

}
