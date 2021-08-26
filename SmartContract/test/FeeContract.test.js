const FeeContract = artifacts.require("FeeContract")

const { assert } = require('chai');
const truffleAssert = require('truffle-assertions');
require('chai').use(require('chai-as-promised')).should()

function calcNewFee(oldFee, users, jurors){
    if(jurors == 0)
        jurors = 1;
    if(users == 0)
        users = 1;

    // oldFee * ratio / targetRatio

    result = oldFee;
    result *= BigInt(users);
    result /= BigInt(jurors);
    result /= BigInt(5); //target ratio

    return result;
}

async function testRatio(feeC, users, jurors){
    var oldPlatformFee = await feeC.getPlatformFee();
    oldPlatformFee = BigInt(oldPlatformFee);

    await feeC.updatePlatformFee(users, jurors);

    var platformFee = await feeC.getPlatformFee();
    platformFee = BigInt(platformFee);

    console.log(platformFee);
    var expectedFee = calcNewFee(oldPlatformFee, users,jurors);
    console.log(expectedFee);

    assert(platformFee == expectedFee, "Invalid Platform fee update");
}

contract('FeeContract', (accounts) =>{
    describe("FeeContract users change", async () =>{
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
            await testRatio(feeC, 25, 5);
        })

        it("users ratio is over", async() =>{
            await testRatio(feeC, 27, 5);
        })

        it("users ratio is under", async() =>{
            await testRatio(feeC, 20, 5);
        })
    })

    describe("FeeContract jurors change", async () =>{
        var feeC;

        before(async ()=>{
            // accounts[0] is owner
            feeC = await FeeContract.new(accounts[0]);
        })

        it("Increased jurors", async() =>{
            await testRatio(feeC, 25, 6);
        })

        it("Decreased jurors", async() =>{
            await testRatio(feeC, 25, 4);
        })
    })

    describe("FeeContract large values", async () =>{
        var feeC;

        before(async ()=>{
            // accounts[0] is owner
            feeC = await FeeContract.new(accounts[0]);
        })

        it("Standard", async() =>{
            await testRatio(feeC, 5005, 1001);
        })

        it("Decreased jurors", async() =>{
            await testRatio(feeC, 5005, 950);
        })

        it("Increased jurors", async() =>{
            await testRatio(feeC, 5005, 1500);
        })

        it("Increased users", async() =>{
            await testRatio(feeC, 10005, 1001);
        })
    })

    describe("FeeContract extreme values", async () =>{
        var feeC;

        before(async ()=>{
            // accounts[0] is owner
            feeC = await FeeContract.new(accounts[0]);
        })

        it("No jurors", async() =>{
            await testRatio(feeC, 10, 0);
        })

        it("Nobody", async() =>{
            await testRatio(feeC, 0, 0);
        })

        it("Not nobody", async() =>{
            await testRatio(feeC, 100, 1);
        })

        it("No users", async() =>{
            await testRatio(feeC, 0, 5);
        })
    })

})