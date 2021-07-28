const { assert } = require('chai')

const UnisonToken = artifacts.require("UnisonToken")
const Verifier = artifacts.require("Verifier")
const RandomSource = artifacts.require("Randomness/RandomSource")

require('chai').use(require('chai-as-promised')).should()

contract('Verifier', (accounts) =>{
    // Unit tests for smart contract go here



    describe("Verifier unit tests", async () =>{

        let verifier

        before(async () =>{
            token = await UnisonToken.new()
            r = await RandomSource.new();
            verifier = await Verifier.new(token.address, r.address);
        })

        it("Can create agreement", async () =>{
            verifier.createAgreement(accounts[1], 0, "do nothing with this agreement");

            var agree = await verifier.getAgreement(0)

            assert.equal(agree.party1, accounts[0])
            assert.equal(agree.party2, accounts[1])
            assert.equal(agree.state, 1)

        })

        it("Can't accept someone else's agreement", async () =>{
            verifier.acceptAgreement(0, {from: accounts[2]})

            var agree = await verifier.getAgreement(0)
            assert.equal(agree.state, 1)

        })

        it("Can accept agreement", async () =>{
            verifier.acceptAgreement(0, {from: accounts[1]})

            var agree = await verifier.getAgreement(0)
            assert.equal(agree.state, 3)

        })

        it("Can pay platform fee", async () =>{
            var agree = await verifier.getAgreement(0);
            var mustPay = agree.platformFee

            token.approve(verifier.address, mustPay);
            verifier.payPlatformFee(0);

            var agree = await verifier.getAgreement(0);
            assert(agree.feePaid == agree.platformFee);

        })

        it("Vote on agreement", async()=>{
            verifier.voteResolution(0, 2, {from: accounts[0]});
            verifier.voteResolution(0, 2, {from: accounts[1]});


            var agree = await verifier.getAgreement(0);

            assert.equal(agree.party1Vote, 2)
            // assert.equal(agree.party2Vote, 2)
        })
    })

    // describe("Verifier unit tests 2", async()=>{
    //     let verifier

    //     before(async () =>{
    //         token = await UnisonToken.new()
    //         r = await RandomSource.new();
    //         verifier = await Verifier.new(token.address, r.address);

    //         // Create a completed agreement for following tests
    //         verifier.createAgreement(accounts[1], 0, "For 2nd round of testing");
    //         verifier.acceptAgreement(0, {from: accounts[1]})

    //         var agree = await verifier.getAgreement(0);
    //         var mustPay = agree.platformFee

    //         token.approve(verifier.address, mustPay);
    //         verifier.payPlatformFee(0);


    //         // Add enough potential members to jury
    //         verifier.addJuror({from: accounts[3]});
    //         verifier.addJuror({from: accounts[4]});
    //         verifier.addJuror({from: accounts[5]});
    //         verifier.addJuror({from: accounts[6]});
    //         verifier.addJuror({from: accounts[7]});
    //         verifier.addJuror({from: accounts[8]});
    //         verifier.addJuror({from: accounts[9]});

    //     })
       
    //     it("Vote on agreement", async()=>{
    //         // Both parties vote no
    //         verifier.voteResolution(0, 1, {from: accounts[0]});
    //         verifier.voteResolution(0, 1, {from: accounts[1]});


    //         var agree = await verifier.getAgreement(0)
    //         assert.equal(agree.party1Vote, 1, "incorrect vote in Agreement")
    //         assert.equal(agree.party2Vote, 1, "incorrect vote in Agreement")
    //         assert.equal(agree.hasJury, true, "Jury wasn't assigned");
    //     })
    // })
})