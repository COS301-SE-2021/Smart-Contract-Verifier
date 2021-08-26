const FeeContract = artifacts.require("FeeContract")

const { assert } = require('chai');
const truffleAssert = require('truffle-assertions');
require('chai').use(require('chai-as-promised')).should()

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
    })

})