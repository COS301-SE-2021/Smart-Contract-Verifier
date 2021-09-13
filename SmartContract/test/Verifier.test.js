const { assert } = require('chai')

const {giveJurorsCoins} = require("./helper.js")

const UnisonToken = artifacts.require("UnisonToken")
const Verifier = artifacts.require("Verifier")
const RandomSource = artifacts.require("Randomness/RandomSource")

const truffleAssert = require('truffle-assertions');
require('chai').use(require('chai-as-promised')).should()

async function createActiveAgreement(verifier, accounts){
    // Create agreement
    var amount = 100
    await verifier.createAgreement(accounts[1], 0, "Will be used for jury testing", "", [token.address], [amount], [true]);

    // Pay in tokens
    await token.approve(verifier.address, amount);
    await verifier.payIn(0);

    // Accept
    await verifier.acceptAgreement(0, {from: accounts[1]})

    // Pay platofrm fee
    var agree = await verifier.getAgreement(0);
    var mustPay = agree.platformFee

    await token.approve(verifier.address, mustPay);
    await verifier.payPlatformFee(0);  
} 

async function prepareJurors(verifier, token, accounts, start, stop){
    // Distribute UnisonToken
    needCoins = [];
    for(var i = start; i<stop; i++){
        needCoins.push(accounts[i]);
    }
    giveJurorsCoins(token, accounts[0], needCoins, 1000000);
}


contract('Verifier', (accounts) =>{
    // Unit tests for smart contract go here

    describe("Verifier unit tests", async () =>{

        var verifier;
        var token;

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

        it("Can't create agreement without payment", async () =>{

            // try{
            //     verifier.createAgreement(accounts[1], 0, "this agreement shouldn't exist", "", [], [], []);
            //     assert(false, "Managed to create an agreement with no payments");       
            // }
            // catch{}
            // var agree = await verifier.getAgreement(0);

            // assert(agree.party1 == "0x0000000000000000000000000000000000000000");
        })

        it("Can create agreement", async () =>{

            var amount = 1000;
            verifier.createAgreement(accounts[1], 0, "do nothing with this agreement", "", [token.address], [amount], [true]);

            var agree = await verifier.getAgreement(0)

            assert.equal(agree.party1, accounts[0])
            assert.equal(agree.party2, accounts[1])
            assert.equal(agree.state, 1)

            var payment = agree.payments[0];
            assert(payment.token == token.address, "Payment has wrong token");
            assert(payment.from == accounts[0], "Payment from wrong address");
            assert(payment.to == accounts[1], "Payment to wrong address");
            var amountInPayment = BigInt(payment.amount);
            assert(amountInPayment == BigInt(1000), "Payment has wrong amount");
            assert(payment.paidIn == false, "Payment marked as paid in");

            await token.approve(verifier.address, amount);
            await verifier.payIn(0);

        })

        it("pay in", async () =>{
            var agree = await verifier.getAgreement(0);
            var payment = agree.payments[0];
            
            await token.approve(verifier.address, payment.amount);
            await verifier.payIn(0);

            var agree = await verifier.getAgreement(0);
            var payment = agree.payments[0];
            assert(payment.paidIn == true, "Payment wasn't registered");

        })

        it("Can't accept someone else's agreement", async () =>{
            try{
                await verifier.acceptAgreement(0, {from: accounts[2]})
                assert.equal(false, "acceptAgreement didn't throw an error");
            }
            catch{}

            var agree = await verifier.getAgreement(0)
            assert.equal(agree.state, 1)

        })

        it("Can accept agreement", async () =>{
            await verifier.acceptAgreement(0, {from: accounts[1]})

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

            var balPre = await token.balanceOf(accounts[3]);
            balPre = BigInt(balPre);

            isJ = await verifier.isJuror(accounts[3]);
            assert(isJ == true, "juror seen as non-juror");

            verifier.removeJuror({from: accounts[3]});
            
            var balPost = await token.balanceOf(accounts[3]);
            balPost = BigInt(balPost);
            assert(balPost == balPre + BigInt(10000), "Staked tokens weren't refunded")


            isJ = await verifier.isJuror(accounts[3]);
            assert(isJ == false, "removed juror still seen as juror");

        })

        it("Add jurors ", async()=>{
            // Add enough potential members to jury
            for(var i=2; i<7; i++){
                token.approve(verifier.address, 10000, {from: accounts[i]});
                verifier.addJuror({from: accounts[i]});
            }
        })

        it("Create 2nd agreement ", async()=>{
            // Create new agreement
            var amount = 100;
            verifier.createAgreement(accounts[1], 0, "For jury test", "", [token.address], [amount], [true]);

            await token.approve(verifier.address, amount);
            await verifier.payIn(1);

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

                for(var j=2; j<7; j++){
                    //accounts 3 to 8 (included) are signed up as jurors
                    if(jury.jurors[i] == accounts[j]){
                        found = true;
                        break;
                    }
                }
                assert(found, "Invalid account on jury");

            }
        })

        it("Jury voting Yes", async()=>{
            var juryStart = await verifier.getJury(1);
            var vote = 2;

            agree = await verifier.getAgreement(1);
            var to = agree.payments[0].to;
            var balPre = await token.balanceOf(to);
            balPre = BigInt(balPre);

            // Each juror votes
            for(var i=0; i<juryStart.jurors.length; i++){
                await verifier.jurorVote(1, vote, {from : juryStart.jurors[i]});

            }

            // Check that all votes were recorded properly
            jury = await verifier.getJury(1);
            for(var i=0; i<juryStart.jurors.length; i++)
                assert(jury.votes[i] == vote, "Vote wasn't properly updated");


            agree = await verifier.getAgreement(1);
            // console.log(agree);
            // agree.state 9 means CLOSED
            assert(agree.state == 9, "Agreement wasn't closed on unanimous YES vote") 

            var balPost = await token.balanceOf(to);
            balPost = BigInt(balPost);

            assert(balPost - balPre == agree.payments[0].amount, "Agreement didn't pay out")


        })

    })

    describe("Verifier unit tests 2", async () =>{

        var verifier

        before(async () =>{
            token = await UnisonToken.new()
            r = await RandomSource.new();
            verifier = await Verifier.new(token.address, r.address);

            prepareJurors(verifier, token, accounts, 1, 10);

            // Create agreement
            var amount = 100;
            await verifier.createAgreement(accounts[1], 0, "do nothing with this agreement", "", [token.address, token.address],
                 [amount, amount], [true, false]);
        })

        it("Need allowance for payment condition", async()=>{
            var agree = await verifier.getAgreement(0);
            var numPaymentsAlready = agree.payments.length;


            // This should throw an error
            try{
            await verifier.payIn(0);
            }
            catch{}

            agree = await verifier.getAgreement(0);

        })        


        it("Pay In", async()=>{
            var amount = 100
            await token.approve(verifier.address, amount);
            await verifier.payIn(0);

            agree = await verifier.getAgreement(0);

            var payment = agree.payments[0];
            assert(payment.paidIn == true, "Payment wasn't registered");
        })

        it("pay In from party2", async()=>{
            var agree = await verifier.getAgreement(0);
            var numPaymentsAlready = agree.payments.length;

            var amount = 100
            await token.approve(verifier.address, amount, {from : accounts[1]});
            await verifier.payIn(0, {from : accounts[1]});

            agree = await verifier.getAgreement(0);

            var payment = agree.payments[1];
            assert(payment.paidIn == true, "Payment wasn't registered");
        })
    })

    describe("Verifier unit tests 3", async () =>{

        var verifier

        before(async () =>{
            token = await UnisonToken.new()
            r = await RandomSource.new();
            verifier = await Verifier.new(token.address, r.address);

            await createActiveAgreement(verifier, accounts);

            prepareJurors(verifier, token, accounts, 1, 10)

            for(var i=3; i<9; i++){
                token.approve(verifier.address, 10000, {from: accounts[i]});
                verifier.addJuror({from: accounts[i]});
            }

            // Contest agreement
            result = await verifier.voteResolution(0, 1, {from: accounts[1]});

        })

        it("Agreement is contested", async()=>{
            var agree = await verifier.getAgreement(0);
            assert(agree.state == 7, "Agreement is not in contested state");

            var jury = await verifier.getJury(0);
            assert(jury.jurors.length > 0, "Jury wasn't assigned");
        })

        it("1 evidence file", async()=>{
            await verifier.addEvidence(0, "file", 42);
            var evidence = await verifier.getEvidence(0);
            assert(evidence.url[0] == "file", "file url wrong in evidence");
            assert(evidence.evidenceHash[0] == 42, "file hash wrong in evidence");
        })

        it("evidence from party2", async()=>{
            await verifier.addEvidence(0, "file2", 101, {from : accounts[1]});
            var evidence = await verifier.getEvidence(0);
            assert(evidence.url[1] == "file2", "file url wrong in evidence")
            assert(evidence.evidenceHash[1] == 101, "file hash wrong in evidence")
        })

        it("Jury votes NO", async ()=>{
            var juryStart = await verifier.getJury(0);
            var vote = 1;

            agree = await verifier.getAgreement(0);
            var from = agree.payments[0].from;
            var balPre = await token.balanceOf(from);
            balPre = BigInt(balPre);
            
            // Each juror votes
            for(var i=0; i<juryStart.jurors.length; i++){
                await verifier.jurorVote(0, vote, {from : juryStart.jurors[i]});

            }

            // Check that all votes were recorded properly
            jury = await verifier.getJury(0);
            for(var i=0; i<juryStart.jurors.length; i++)
                assert(jury.votes[i] == vote, "Vote wasn't properly updated");


            agree = await verifier.getAgreement(0);
            // console.log(agree);
            // agree.state 9 means CLOSED
            assert(agree.state == 9, "Agreement wasn't closed on unanimous NO vote") 

            var balPost = await token.balanceOf(from);
            balPost = BigInt(balPost);

            assert(balPost - balPre == agree.payments[0].amount, "Agreement didn't refund")     
        })

    })

    describe("Verifier unit tests 4", async () =>{

        var verifier

        // Similar to previous test, except payment is going from part2 to party1

        before(async () =>{
            token = await UnisonToken.new()
            r = await RandomSource.new();
            verifier = await Verifier.new(token.address, r.address);

            prepareJurors(verifier, token, accounts, 1, 10);

            // Create agreement
            var amount = 100;
            await verifier.createAgreement(accounts[1], 0, "Will be used for jury testing", "", [token.address], [amount], [false]);

            // Pay in
            await token.approve(verifier.address, amount, {from : accounts[1]});
            await verifier.payIn(0, {from : accounts[1]});

            // Accept
            await verifier.acceptAgreement(0, {from: accounts[1]})

            // Pay platofrm fee
            var agree = await verifier.getAgreement(0);
            var mustPay = agree.platformFee;

            await token.approve(verifier.address, mustPay);
            await verifier.payPlatformFee(0);  

            for(var i=3; i<9; i++){
                token.approve(verifier.address, 10000, {from: accounts[i]});
                verifier.addJuror({from: accounts[i]});
            }

            // Contest agreement
            result = await verifier.voteResolution(0, 1, {from: accounts[1]});

            var jury = await verifier.getJury(0);
            assert(jury.jurors.length > 0, "Jury wasn't assigned");
        })

        it("Jury votes NO", async ()=>{
            var juryStart = await verifier.getJury(0);
            var vote = 1;

            agree = await verifier.getAgreement(0);
            var from = agree.payments[0].from;
            var balPre = await token.balanceOf(from);
            balPre = BigInt(balPre);
            
            // Each juror votes
            for(var i=0; i<juryStart.jurors.length; i++){
                await verifier.jurorVote(0, vote, {from : juryStart.jurors[i]});

            }

            // Check that all votes were recorded properly
            jury = await verifier.getJury(0);
            for(var i=0; i<juryStart.jurors.length; i++)
                assert(jury.votes[i] == vote, "Vote wasn't properly updated");


            agree = await verifier.getAgreement(0);
            // console.log(agree);
            // agree.state 9 means CLOSED
            assert(agree.state == 9, "Agreement wasn't closed on unanimous NO vote") 

            var balPost = await token.balanceOf(from);
            balPost = BigInt(balPost);

            assert(balPost - balPre == agree.payments[0].amount, "Agreement didn't refund")     
        })

    })


    describe("Verifier unit tests 5", async () =>{

        var verifier

        before(async () =>{
            token = await UnisonToken.new()
            r = await RandomSource.new();
            verifier = await Verifier.new(token.address, r.address);

            await createActiveAgreement(verifier, accounts);

            prepareJurors(verifier, token, accounts, 1, 10)

            for(var i=3; i<9; i++){
                token.approve(verifier.address, 10000, {from: accounts[i]});
                verifier.addJuror({from: accounts[i]});
            }

            // Contest agreement
            result = await verifier.voteResolution(0, 1, {from: accounts[1]});

        })

        it("Outsider can't add evidence", async ()=>{
            try{
                await verifier.addEvidence(0, "file", 42, {from : accounts[2]});
                assert(false, "addEvidence didn't thow an error");
            }
            catch{}
            var evidence = await verifier.getEvidence(0);
            assert(evidence.url.length == 0, "file url was added by outsider")
            assert(evidence.evidenceHash.length == 0, "file hash was added by outsider")
        })

        it("multiple evidence files", async ()=>{
            try{
                await verifier.addEvidence(0, "file", 42);
                await verifier.addEvidence(0, "another file", 43);
                assert(false, "addEvidence didn't thow an error");
            }
            catch{}
            
            var evidence = await verifier.getEvidence(0);
            assert(evidence.url.length == 2, "wrong amount of evidence");
            assert(evidence.evidenceHash.length == 2, "wrong amount of evidence");

            assert(evidence.url[0] == "file", "file url wrong in evidence")
            assert(evidence.evidenceHash[0] == 42, "file hash wrong in evidence")

            assert(evidence.url[1] == "another file", "file url wrong in evidence")
            assert(evidence.evidenceHash[1] == 43, "file hash wrong in evidence")
        })

        it("multiple evidence files from both parties", async ()=>{
            try{
                await verifier.addEvidence(0, "file 3", 10, {from : accounts[1]});
                await verifier.addEvidence(0, "file 4", 11);
                assert(false, "addEvidence didn't thow an error");
            }
            catch{}
            
            var evidence = await verifier.getEvidence(0);
            assert(evidence.url.length == 4, "wrong amount of evidence");
            assert(evidence.evidenceHash.length == 4, "wrong amount of evidence");

            assert(evidence.url[2] == "file 3", "file url wrong in evidence")
            assert(evidence.evidenceHash[2] == 10, "file hash wrong in evidence")

            assert(evidence.url[3] == "file 4", "file url wrong in evidence")
            assert(evidence.evidenceHash[3] == 11, "file hash wrong in evidence")
        })
        
    })

    describe("Verifier unit tests 6", async () =>{

        var verifier;
        var token;

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

        it("Reject an agreement 1", async () =>{
            var amount = 100;
            await verifier.createAgreement(accounts[1], 0, "Will be used for jury testing", "", [token.address], [amount], [false]);

            var balPre = await token.balanceOf(accounts[0]);
            balPre = BigInt(balPre);

            var bal2Pre = await token.balanceOf(accounts[1]);
            bal2Pre = BigInt(bal2Pre);

            verifier.rejectAgreement(0);

            var balPost = await token.balanceOf(accounts[0]);
            balPost = BigInt(balPost);

            var bal2Post = await token.balanceOf(accounts[1]);
            bal2Post = BigInt(bal2Post);

            agree = await verifier.getAgreement(0);

            assert(balPre == balPost, "Payment condition that was never paid has been refunded");
            assert(bal2Pre == bal2Post, "Payment condition that was never paid has been paid out");
            assert(agree.state == 2, "Agreement not in REJECTED state");
        })

        it("Reject an agreement 2", async () =>{
            var amount = 100;
            await verifier.createAgreement(accounts[1], 0, "Will be used for jury testing", "", [token.address], [amount], [true]);

            var balPre = await token.balanceOf(accounts[0]);
            balPre = BigInt(balPre);

            var bal2Pre = await token.balanceOf(accounts[1]);
            bal2Pre = BigInt(bal2Pre);

            verifier.rejectAgreement(1);

            var balPost = await token.balanceOf(accounts[0]);
            balPost = BigInt(balPost);

            var bal2Post = await token.balanceOf(accounts[1]);
            bal2Post = BigInt(bal2Post);

            agree = await verifier.getAgreement(1);

            assert(balPre == balPost, "Payment condition that was never paid has been paid out");
            assert(bal2Pre == bal2Post, "Payment condition that was never paid has been refunded");
            assert(agree.state == 2, "Agreement not in REJECTED state");
        })

        it("Reject an agreement that's paid in", async () =>{
            var amount = 100;
            await verifier.createAgreement(accounts[1], 0, "Will be used for jury testing", "", [token.address], [amount], [true]);

            // Pre balance is checked before paid in
            var balPre = await token.balanceOf(accounts[0]);
            balPre = BigInt(balPre);

            var bal2Pre = await token.balanceOf(accounts[1]);
            bal2Pre = BigInt(bal2Pre);

            await token.approve(verifier.address, amount);
            verifier.payIn(2);

            verifier.rejectAgreement(2);

            var balPost = await token.balanceOf(accounts[0]);
            balPost = BigInt(balPost);

            var bal2Post = await token.balanceOf(accounts[1]);
            bal2Post = BigInt(bal2Post);

            agree = await verifier.getAgreement(2);

            assert(balPre == balPost, "Payment condition that was never paid has been paid out");
            assert(bal2Pre == bal2Post, "Payment condition that was never paid has been refunded");
            assert(agree.state == 2, "Agreement not in REJECTED state");
        })

    })
})