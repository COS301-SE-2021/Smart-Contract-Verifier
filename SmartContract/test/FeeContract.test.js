const FeeContract = artifacts.require("FeeContract")

const { assert } = require('chai');
const truffleAssert = require('truffle-assertions');
require('chai').use(require('chai-as-promised')).should()

function calcNewFee(oldFee, users, jurors){
    // oldFee * ratio / targetRatio

    result = oldFee;
    result *= BigInt(users);
    result /= BigInt(jurors);
    result /= BigInt(5); //target ratio

    return result;
}

contract('FeeContract', (accounts) =>{
    describe("FeeContract unit tests", async () =>{
        var feeC;

        before(async ()=>{
            // accounts[0] is owner
            feeC = await FeeContract.new(accounts[0]);
        })

        it("Test getters", async ()=>{
            // Check initial values
            var platformFee = await feeC.getPlatformFee();
            platformFee = BigInt(platformFee);
            assert(platformFee == 1000000000, "issue with getPlatformFee");

            var stakingAmount = await feeC.getStakingAmount();
            stakingAmount = BigInt(stakingAmount);
            assert(stakingAmount == 10000, "issue with getStakingAmount");
        })

        it("users ratio is correct", async() =>{
            var oldPlatformFee = await feeC.getPlatformFee();
            oldPlatformFee = BigInt(oldPlatformFee);

            await feeC.updatePlatformFee(25, 5);

            var platformFee = await feeC.getPlatformFee();
            platformFee = BigInt(platformFee);

            assert(platformFee == oldPlatformFee, "Platform fee changed when ratio was correct");

        })

        it("users ratio is over", async() =>{
            var oldPlatformFee = await feeC.getPlatformFee();
            oldPlatformFee = BigInt(oldPlatformFee);

            await feeC.updatePlatformFee(26, 5);

            var platformFee = await feeC.getPlatformFee();
            platformFee = BigInt(platformFee);

            console.log(platformFee);

            var expectedFee = calcNewFee(oldPlatformFee, 26,5);
            console.log(expectedFee);

            assert(platformFee == expectedFee, "Platform fee changed when ratio was correct");
        })
    })

})