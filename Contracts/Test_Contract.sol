pragma solidity 0.6.6;
//This is simply to test access from Flutter
//Made in IDEA, deployed with REM

contract SCV {

    string public testString;

    /// Create a new ballot to choose one of `proposalNames`.
    constructor() public {
        testString = "This is from within the smart contract!";
    }

    function getData() view public returns( string memory) {
        return testString;
    }
    function setData(string memory dat) public {
        testString = dat;
    }

}
