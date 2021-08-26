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

        it("Test 1", async ()=>{
            
        })
    })

})