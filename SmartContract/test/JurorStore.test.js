const { assert } = require('chai')

const truffleAssert = require('truffle-assertions');

const JurorStore = artifacts.require("JurorStore")
const RandomSource = artifacts.require("Randomness/RandomSource")

require('chai').use(require('chai-as-promised')).should()

contract('JurorStore', (accounts) =>{
    
    describe("JurorStore unit tests", async () =>{
        let jurorStore

        // In these unit tests, accounts[0] is the owner of JurorStore
        before(async () =>{
            r = await RandomSource.new(); 

            jurorStore = await JurorStore.new(accounts[0], r.address, {from: accounts[0]});
        })

        it("Can add a juror", async() =>{
            var passedRequire = false;
            let result;
            try{
                result = await jurorStore.addJuror(accounts[1], {from: accounts[0]});
                passedRequire = true;
            }
            catch(e){}

            assert(passedRequire == true, "Could not add juror");
        })

        it("Can't add a juror as non-owner", async() =>{
            var passedRequire = false;
            try{
                let result = await jurorStore.addJuror(accounts[1], {from: accounts[1]});
                passedRequire = true;
            }
            catch(e){}
            assert(passedRequire == false, "Could add juror as non-owner");

        })


        it("AssignJury assigns a jury", async() =>{
            // accounts[1] already added as a juror in a previous test
            for(var i=2; i<10; i++){
                await jurorStore.addJuror(accounts[i], {from: accounts[0]});

            }
  
            await jurorStore.assignJury(1, BigInt(12351),[], {from:accounts[0]});
        })

    })


    describe("assignJury detailed testing", async () =>{
        let jurorStore

        // In these unit tests, accounts[0] is the owner of JurorStore
        before(async () =>{
            r = await RandomSource.new(); 

            jurorStore = await JurorStore.new(accounts[0], r.address, {from: accounts[0]});
        })


        it("too few available jurors", async() =>{
            // Add one juror too few

            for(var i=2; i<5; i++){
                await jurorStore.addJuror(accounts[i], {from: accounts[0]});

            }
  
            try{
                await jurorStore.assignJury(5, BigInt(12351),[], {from:accounts[0]});
                assert(false, "Jury was assigned when it shouldn't have been possible");
            }
            catch{}
        })

        it("Enough jurors, but some are in noUse list", async()=>{
            await jurorStore.addJuror(accounts[1], {from: accounts[0]});

            try{
                await jurorStore.assignJury(5, BigInt(12351),[accounts[1]], {from:accounts[0]});
                assert(false, "Jury was assigned when it shouldn't have been possible");
            }
            catch{}
        })

    })


    describe("juror strikes", async () =>{
        let jurorStore

        // In these unit tests, accounts[0] is the owner of JurorStore
        before(async () =>{
            r = await RandomSource.new(); 

            jurorStore = await JurorStore.new(accounts[0], r.address, {from: accounts[0]});
            await jurorStore.addJuror(accounts[0], {from: accounts[0]});

        })


        it("Juror removed after 3 strikes", async() =>{
            var isJuror = await jurorStore.isJuror(accounts[0]);
            assert(isJuror == true, "Precondition not met, couldn't perform test");

            for(var i=0; i<3; i++)
                jurorStore.addStrike(accounts[0]);

            isJuror = await jurorStore.isJuror(accounts[0]);
            assert(isJuror == false, "Juror wasn't removed after strikes");
        })

    })

})
