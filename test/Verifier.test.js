const { assert } = require('chai')

const Verifier = artifacts.require("Verifier")

require('chai').use(require('chai-as-promised')).should()


contract('Verifier', (accounts) =>{
    // Unit tests for smart contract go here

    let verifier

    before(async () =>{
        verifier = await Verifier.new()
    })

    describe("Verifier", async () =>{
        it("Can get agreement", async () =>{
            verifier.getAgreement(0)
        })
    })

})