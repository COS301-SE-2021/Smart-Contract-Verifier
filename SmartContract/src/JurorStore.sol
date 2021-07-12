// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./Randomness/RandomSource.sol";

contract JurorStore{
    address owner;
    RandomSource randomSource;

    uint numJurors;
    address[] jurors;
    mapping(address => uint256) jurorIndex;

    // Restricts usage of any function with this modifier to the owner (the Verifier contract)
    modifier onlyOwner(){
        require(msg.sender == owner);
        _;
    }

    constructor(address _owner, RandomSource rs){
        owner = _owner;
        randomSource = rs;

        // index 0 is kept out of use since a 0 in the mapping means that value doesn't exist
        jurors.push(address(0));
    }

    function addJuror(address j) public onlyOwner(){
        uint256 index = jurors.length;
        jurors.push(j);
        numJurors++;
        jurorIndex[j] = index;
        emit AddJuror(j, index);
    }

    function removeJuror(address j) public onlyOwner(){
        // Address 0 can't be removed since:
        // a) it isn't in jurorIndex so it's index is 0
        // b) It's index really is 0 so it would return even if it were in the mapping

        uint256 index = jurorIndex[j];
        if(index == 0)
            return;
        
        uint256 last = jurors.length -1;

        if(index != last)
            jurors[index] = jurors[last];

        jurors.pop();
        numJurors--;
        jurorIndex[j] = 0;

        emit RemoveJuror(j);
    }

    // Cleared after every execution of assignJury
    // It's only here since mappings can't be declared in a function scope
    mapping(address =>bool) assignedJurors;

    // Assigns a jury of specified size, if possible
    function assignJury(uint count, uint seed) public onlyOwner() returns(address[] memory jury){
        require(count > 0, "Jury size must be a positive value");
        require(count <= numJurors, "Jury size is too big, not enough jurors available");

        address[] memory result = new address[](count);

        for(uint i=0; i<count; i++){
            bool foundVal = false;
            address j;
            while(!foundVal){
                uint val = randomSource.getRandVal(seed) % numJurors;
                seed++;
                j = jurors[val];
                if(assignedJurors[j] == false)
                    foundVal = true;
            }

            assignedJurors[j] = true;
            result[i] = j;
        }

        for(uint i=0; i<count; i++){
            assignedJurors[result[i]] = false;
        }

        return result;
    }

    event AddJuror(address juror, uint256 index);
    event RemoveJuror(address juror);

}
