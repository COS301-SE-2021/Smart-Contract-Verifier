const { assert } = require('chai')

const Verifier = artifacts.require("Verifier")

require('chai').use(require('chai-as-promised')).should()


contract('Verifier', (accounts) =>{
    // Unit tests for smart contract go here



    describe("Verifier positive tests", async () =>{

        let verifier

        before(async () =>{
            verifier = await Verifier.new()
        })

        it("Can create agreement", async () =>{
            const timestamp = Math.round(Date.now() / 1000)
            verifier.createAgreement(accounts[1], timestamp + 10)

            var agree = await verifier.getAgreement(0)

            assert.equal(agree.party1, accounts[0])
            assert.equal(agree.party2, accounts[1])
            assert.equal(agree.accepted, false)

        })

        it("Can accept agreement", async () =>{
            verifier.acceptAgreement(0, {from: accounts[1]})

            var agree = await verifier.getAgreement(0)
            assert.equal(agree.accepted, true)

        })
    })


    describe("Verifier negative tests", async () =>{
        // Runs on a new Verifier unaffected by data from previous tests
        
        let verifier

        before(async () =>{
            verifier = await Verifier.new()
 
            const timestamp = Math.round(Date.now() / 1000)
            verifier.createAgreement(accounts[1], timestamp + 10)
        })

        it("Can't accept someone else's agreement", async () =>{
            verifier.acceptAgreement(0, {from: accounts[2]})

            var agree = await verifier.getAgreement(0)
            assert.equal(agree.accepted, false)

        })
    })

})