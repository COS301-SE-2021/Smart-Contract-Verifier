const { assert } = require('chai')

const JurorStore = artifacts.require("JurorStore")

require('chai').use(require('chai-as-promised')).should()

contract('JurorStore', (accounts) =>{
    
    describe("JurorStore unit tests", async () =>{
        let jurorStore

        before(async () =>{
            jurorStore = await JurorStore.new(accounts[0], {from: accounts[0]});
        })


    })

})
