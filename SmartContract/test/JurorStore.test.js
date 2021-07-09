const { assert } = require('chai')

const truffleAssert = require('truffle-assertions');

const JurorStore = artifacts.require("JurorStore")

require('chai').use(require('chai-as-promised')).should()

contract('JurorStore', (accounts) =>{
    
    describe("JurorStore unit tests", async () =>{
        let jurorStore

        // In these unit tests, accounts[0] is the owner of JurorStore
        before(async () =>{
            jurorStore = await JurorStore.new(accounts[0], {from: accounts[0]});
        })

        it("Can add a juror", async() =>{
            let result = await jurorStore.addJuror(accounts[1], {from: accounts[0]});

            truffleAssert.eventEmitted(result, "AddJuror", (ev)=>{
                return ev.juror == accounts[1]
            });
        })

    })

})
