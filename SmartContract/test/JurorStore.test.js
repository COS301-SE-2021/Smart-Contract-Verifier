const { assert } = require('chai')

const JurorStore = artifacts.require("JurorStore")

require('chai').use(require('chai-as-promised')).should()

contract('JurorStore', (accounts) =>{
    
    describe("JurorStore unit tests", async () =>{
        let jurorStore

        before(async () =>{
            jurorStore = await JurorStore.new(accounts[0], {from: accounts[0]});
        })

        it("Can't use JurorStore as non-owner", async()=>{
            // This tests the onlyOwner modifier
          
            var val = await jurorStore.getTempVal();
            assert(val == false, "Precondition not satisfied");

            try{
            await jurorStore.addJuror(accounts[0], {from: accounts[1]});
            }
            catch(error){}

            val = await jurorStore.getTempVal();
            assert(val == false, "onlyOwner modifier didn't work");

        })

        it("Can use JurorStore as owner", async()=>{
            // This tests the onlyOwner modifier
          
            var val = await jurorStore.getTempVal();
            assert(val == false, "Precondition not satisfied");

            try{
            await jurorStore.addJuror(accounts[0], {from: accounts[0]});
            }
            catch(error){}

            val = await jurorStore.getTempVal();
            assert(val == true, "onlyOwner modifier didn't work");

        })

    })

})
