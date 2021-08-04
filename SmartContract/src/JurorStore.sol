// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./Utilities/RandomSource.sol";
import "./Token/IERC20.sol";

contract JurorStore{
    address owner;
    RandomSource randomSource;
    IERC20 token;

    uint numJurors;
    address[] jurors;
    mapping(address => uint256) jurorIndex;

    // Restricts usage of any function with this modifier to the owner (the Verifier contract)
    modifier onlyOwner(){
        require(msg.sender == owner);
        _;
    }

    constructor(address _owner, RandomSource rs, IERC20 _token){
        owner = _owner;
        randomSource = rs;
        token = _token;

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
    mapping(uint =>bool) assignedJurors;

    // Assigns a jury of specified size, if possible
    // noUse contains addresses that can't be jurors for this case.
    // parties involved in agreement should be put in noUse list
    function assignJury(uint count, uint seed, address[] calldata noUse)
             public onlyOwner() returns(address[] memory jury){
        require(count > 0, "Jury size must be a positive value");
        require(count <= numJurors, "Jury size is too big, not enough jurors available");

        uint[] memory indices = new uint[](count);
        address[] memory result = new address[](count);

        uint val = randomSource.getRandVal(seed);

        uint noUseLen = noUse.length;
        for(uint i=0; i<count; i++){
            uint index = val % numJurors;
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
                if(!valid)
                    index++;
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

    event AddJuror(address juror, uint256 index);
    event RemoveJuror(address juror);

}
