pragma solidity 0.6.6;
//This is simply to test access from Flutter
//Made in IDEA, deployed with REM
//Current version actually uses uint

contract SCV {

    uint public testNum;

    /// Create a new ballot to choose one of `proposalNames`.
    constructor() public {
        testNum = 3;
    }

    function getData() view public returns( uint) {
        return testNum;
    }
    function setData(uint dat) public {
        testNum = dat;
    }

}
