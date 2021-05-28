pragma solidity >=0.4.22 <0.9.0;

contract Hello{
    string public message = "Hello world!";

    function setMessage(string memory newMsg) public{
        message = newMsg;
    }

}