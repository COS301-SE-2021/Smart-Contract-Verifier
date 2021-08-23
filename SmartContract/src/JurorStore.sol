// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./Utilities/RandomSource.sol";

contract JurorStore{
    address owner;
    RandomSource randomSource;

    uint numJurors;
    address[] jurors;
    mapping(address => uint256) jurorIndex;
    mapping(address => uint256) strikes;

    uint constant maxStrikes = 3;

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

    function addJuror(address j) public onlyOwner() returns(uint256){
        require(jurorIndex[j] == 0, "You are already a juror");
        require(strikes[j] < maxStrikes, "You have been banned from being a juror");

        uint256 index = jurors.length;
        jurors.push(j);
        numJurors++;
        jurorIndex[j] = index;
        return index;
    }

    function isJuror(address a) public view returns(bool){
        return (jurorIndex[a] != 0);
    }

    function removeJuror(address j) public onlyOwner(){
        // Address 0 can't be removed since:
        // a) it isn't in jurorIndex so it's index is 0
        // b) It's index really is 0 so it would return even if it were in the mapping

        uint256 index = jurorIndex[j];
        require(index != 0, "You need to be a juror in the first place in order to be removed");
        
        uint256 last = jurors.length -1;

        if(index != last)
            jurors[index] = jurors[last];

        jurors.pop();
        numJurors--;
        jurorIndex[j] = 0;
    }


    // record strike against juror. If max strikes has been reached,zremove juror from pool
    function addStrike(address j) public onlyOwner() returns(bool){
        require(jurorIndex[j] > 0, "Specified juror is not currently in the system");

        strikes[j]++;
        if(strikes[j] >= maxStrikes){
            removeJuror(j);
            return true;
        }
        return false;
    }

    // Cleared after every execution of assignJury
    // It's only here since mappings can't be declared in a function scope
    mapping(uint =>bool) assignedJurors;

    // Assigns a jury of specified size, if possible
    // noUse contains addresses that can't be jurors for this case.
    // parties involved in agreement should be put in noUse list
    function assignJury(uint count, uint seed, address[] calldata noUse)
             public onlyOwner() returns(address[] memory jury){
        require(count > 0, "Jury size must be a positive value");

        uint numAvailableJurors = numJurors;
        uint noUseLen = noUse.length;

        for(uint i=0; i<noUseLen; i++){
            if(jurorIndex[noUse[i]] != 0)
                numAvailableJurors--;
        }

        require(count <= numAvailableJurors, "Jury size is too big, not enough jurors available");

        uint[] memory indices = new uint[](count);
        address[] memory result = new address[](count);

        uint val = randomSource.getRandVal(seed);


        for(uint i=0; i<count; i++){
            uint index = val % numJurors + 1;
            bool valid = false;

            while(!valid){
                if(!assignedJurors[index]){
                    valid = true;

                    for(uint j=0; j<noUseLen; j++){
                        if(jurors[index] == noUse[j]){
                            valid = false;
                            break;
                        }
                    }
                }
                if(!valid){
                    // Increment by one, but wrap around when end of list is reached
                    index++;
                    if(index == numJurors + 1)
                        index = 1;
                }
            }

            
            assignedJurors[index] = true;
            indices[i] = index;
            result[i] = jurors[index];

            val = val / numJurors;
        }

        for(uint i=0; i<count; i++){
            assignedJurors[indices[i]] = false;
        }

        return result;
    }

}
