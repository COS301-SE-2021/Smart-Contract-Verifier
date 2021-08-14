const { assert } = require('chai')

const {giveJurorsCoins} = require("./helper.js")

const UnisonToken = artifacts.require("UnisonToken")
const Verifier = artifacts.require("Verifier")
const RandomSource = artifacts.require("Randomness/RandomSource")

const truffleAssert = require('truffle-assertions');
require('chai').use(require('chai-as-promised')).should()

contract('Verifier', (accounts) =>{
    // Unit tests for smart contract go here

    describe("Verifier unit tests", async () =>{

        let verifier

        before(async () =>{
            token = await UnisonToken.new()
            r = await RandomSource.new();
            verifier = await Verifier.new(token.address, r.address);

            needCoins = [];
            for(var i = 1; i<9; i++){
                needCoins.push(accounts[i]);
            }
            giveJurorsCoins(token, accounts[0], needCoins, 100000);
        })

        it("Can create agreement", async () =>{

            verifier.createAgreement(accounts[1], 0, "do nothing with this agreement", "");

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
            assert.equal(agree.party2Vote, 2)
        })

        it("isJuror function", async() =>{
            var isJ = await verifier.isJuror(accounts[3]);
            assert(isJ == false, "non-juror seen as juror");

            token.approve(verifier.address, 10000, {from: accounts[3]});
            verifier.addJuror({from: accounts[3]});

            isJ = await verifier.isJuror(accounts[3]);
            assert(isJ == true, "juror seen as non-juror");

            verifier.removeJuror({from: accounts[3]});
            
            isJ = await verifier.isJuror(accounts[3]);
            assert(isJ == false, "removed juror still seen as juror");

        })

        it("Add jurors ", async()=>{
            // Add enough potential members to jury
            for(var i=3; i<9; i++){
                token.approve(verifier.address, 10000, {from: accounts[i]});
                verifier.addJuror({from: accounts[i]});
            }
        })

        it("Create 2nd agreement ", async()=>{
            // Create new agreement
            verifier.createAgreement(accounts[1], 0, "For jury test", "");
            verifier.acceptAgreement(1, {from: accounts[1]})

            var agree = await verifier.getAgreement(1);
            var mustPay = agree.platformFee;

            token.approve(verifier.address, mustPay);
            verifier.payPlatformFee(1);      
        })

        it("Vote no on agreement ", async()=>{
            // console.log("Vote no");

            result = await verifier.voteResolution(1, 1, {from: accounts[0]});


            var agree = await verifier.getAgreement(1);
            var jury = await verifier.getJury(1);
            // console.log(agree);

            assert.equal(agree.party1Vote, 1, "incorrect vote in Agreement")
            assert.equal(jury.assigned, true, "Jury wasn't assigned");

            truffleAssert.eventEmitted(result, "JuryAssigned", (ev)=>{
                return ev.agreeID == 1
            });

        })


        it("Get jury", async()=>{
            var jury = await verifier.getJury(1);

            for(var i=0; i<jury.jurors.length; i++){
                var found = false;

                for(var j=3; j<9; j++){
                    //accounts 3 to 8 (included) are signed up as jurors
                    if(jury.jurors[i] == accounts[j]){
                        found = true;
                        break;
                    }
                }
                assert(found, "Invalid account on jury");

            }
        })

        // it("Jury voting", async()=>{
        //     var jury = await verifier.getJury(1);
        //     var accIndex = -1
        //     for(var i=0; i<10; i++){
        //         if(accounts[i] == jury.jurors[0]){
        //             accIndex = i;
        //             break;
        //         }
        //     }
        //     assert(accIndex != -1, "Juror couldn't be found")

        //     var vote = 2;
        //     await verifier.jurorVote(1, vote, {from : accounts[accIndex]});

        //     jury = await verifier.getJury(1);

        //     for(var i=0; i < jury.jurors.length; i++){
        //         //accounts 3 to 8 (included) are signed up as jurors
        //         if(jury.jurors[i] == accounts[accIndex]){
        //             assert(jury.votes[i] == vote, "Vote wasn't properly updated");
        //             break;
        //         }
        //     } 
        // })

        it("Jury voting", async()=>{
            var juryStart = await verifier.getJury(1);
            var vote = 2;

            // Each juror votes
            for(var i=0; i<juryStart.jurors.length; i++){
                await verifier.jurorVote(1, vote, {from : juryStart.jurors[i]});

            }

            // Check that all votes were recorded properly
            jury = await verifier.getJury(1);
            for(var i=0; i<juryStart.jurors.length; i++)
                assert(jury.votes[i] == vote, "Vote wasn't properly updated");


            agree = await verifier.getAgreement(1);
            // agree.state 9 means CLOSED
            assert(agree.state == 9, "Agreement wasn't closed on unanimous YES vote") 

        })

    })

    describe("Verifier unit tests 2", async () =>{

        let verifier

        before(async () =>{
            token = await UnisonToken.new()
            r = await RandomSource.new();
            verifier = await Verifier.new(token.address, r.address);

            // Create agreement
            verifier.createAgreement(accounts[1], 0, "do nothing with this agreement", "");
            verifier.acceptAgreement(0, {from: accounts[1]})

            // Pay platofrm fee
            var agree = await verifier.getAgreement(0);
            var mustPay = agree.platformFee

            token.approve(verifier.address, mustPay);
            verifier.payPlatformFee(0);
        })

        it("Add payment condition", async()=>{
            var agree = await verifier.getAgreement(0);
            var numPaymentsAlready = agree.payments.length;

            var amount = 100
            token.approve(verifier.address, amount);
            verifier.addPaymentConditions(0, [token.address], [amount]);

            agree = await verifier.getAgreement(0);
            assert(agree.payments.length == numPaymentsAlready + 1);
        })

        it("Add multiple payment conditions", async()=>{
            var agree = await verifier.getAgreement(0);
            var numPaymentsAlready = agree.payments.length;

            var amount = 100
            token.approve(verifier.address, amount*2);
            verifier.addPaymentConditions(0, [token.address, token.address], [amount, amount]);

            agree = await verifier.getAgreement(0);
            assert(agree.payments.length == numPaymentsAlready + 2);
        })

        it("Need allowance for payment condition", async()=>{
            var agree = await verifier.getAgreement(0);
            var numPaymentsAlready = agree.payments.length;

            var amount = 100
            verifier.addPaymentConditions(0, [token.address], [amount]);

            agree = await verifier.getAgreement(0);
            assert(agree.payments.length == numPaymentsAlready);
        })        

    })
})