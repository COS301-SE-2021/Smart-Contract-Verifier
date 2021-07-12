const { assert } = require('chai');

const truffleAssert = require('truffle-assertions');

const RandomSource = artifacts.require("Randomness/RandomSource")

require('chai').use(require('chai-as-promised')).should()

contract('RandomSource', (accounts) =>{
    
    describe("RandomSource unit tests", async () =>{
        let randomSource

        // In these unit tests, accounts[0] is the owner of JurorStore
        before(async () =>{
            randomSource = await RandomSource.new(); 
        })

        it("Can generate seedless random value", async() =>{
            result = await randomSource.getRandVal();
        })

        // it("Can generate random value from seed", async() =>{
        //     result = await randomSource.getRandVal(1);
        // })

    })

})
