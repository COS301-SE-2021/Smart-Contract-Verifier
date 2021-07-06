const { assert } = require('chai')

const UnisonToken = artifacts.require("UnisonToken")
const Verifier = artifacts.require("Verifier")

require('chai').use(require('chai-as-promised')).should()

contract('Verifier', (accounts) =>{
    // Unit tests for smart contract go here



    describe("Verifier unit tests", async () =>{

        let verifier

        before(async () =>{
            token = await UnisonToken.new()
            verifier = await Verifier.new(token.address)
        })

        it("Can create agreement", async () =>{
            const timestamp = Math.round(Date.now() / 1000)
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


            var agree = await verifier.getAgreement(0)
            assert.equal(agree.party1Vote, 2)
            assert.equal(agree.party2Vote, 2)
        })
    })

    describe("Unison token unit tests", async () =>{

        let token

        before(async () =>{
            token = await UnisonToken.new()
        })

        it("Transfer token", async() =>{
            // Transfers a set amount of token from account 0 to account 1 and checks
            // that both balances are updated accurately

            var acc0Before = await token.balanceOf(accounts[0]);
            acc0Before = BigInt(acc0Before);

            var acc1Before = await token.balanceOf(accounts[1]);
            acc1Before = BigInt(acc1Before);

            var transferSize = BigInt(1000)
            token.transfer(accounts[1], transferSize, {from: accounts[0]});

            var acc1After = await token.balanceOf(accounts[1]);
            acc1After = BigInt(acc1After);

            var acc0After = await token.balanceOf(accounts[0]);
            acc0After = BigInt(acc0After);

            assert.equal(acc0Before, acc0After + transferSize);
            assert.equal(acc1Before + transferSize, acc1After);

        })


        it("Total supply", async() =>{
            // Transfers a set amount of token from account 0 to account 1 and checks
            // that the totalSupply doesn't change
            
            var supplyBefore = await token.totalSupply();
            supplyBefore = BigInt(supplyBefore);


            var transferSize = BigInt(1000)
            token.transfer(accounts[1], transferSize, {from: accounts[0]});

            var supplyAfter = await token.totalSupply();
            supplyAfter = BigInt(supplyAfter);

            assert.equal(supplyBefore, supplyAfter);

        })

        it("Can't use transferFrom w/o allowance", async() =>{
            // Attempts to transfer a set amount of token from account 0 to account 1
            // without an allowance, neither balance should change

            var acc0Before = await token.balanceOf(accounts[0]);
            acc0Before = BigInt(acc0Before);

            var acc1Before = await token.balanceOf(accounts[1]);
            acc1Before = BigInt(acc1Before);

            var transferSize = BigInt(1000)

            try{
                await token.transferFrom(accounts[0], accounts[1], transferSize, {from: accounts[2]});
            }
            catch(error){}


            var acc1After = await token.balanceOf(accounts[1]);
            acc1After = BigInt(acc1After);

            var acc0After = await token.balanceOf(accounts[0]);
            acc0After = BigInt(acc0After);

            assert.equal(acc0Before, acc0After);
            assert.equal(acc1Before, acc1After);

        })

        it("Can use transferFrom with allowance", async() =>{
            // Transfers a set amount of token from account 0 to account 1 and checks
            // that both balances are updated accurately

            var acc0Before = await token.balanceOf(accounts[0]);
            acc0Before = BigInt(acc0Before);

            var acc1Before = await token.balanceOf(accounts[1]);
            acc1Before = BigInt(acc1Before);

            var transferSize = BigInt(1000)

            try{
                await token.approve(accounts[2], transferSize, {from: accounts[0]});
                await token.transferFrom(accounts[0], accounts[1], transferSize, {from: accounts[2]});
            }
            catch(error){}


            var acc1After = await token.balanceOf(accounts[1]);
            acc1After = BigInt(acc1After);

            var acc0After = await token.balanceOf(accounts[0]);
            acc0After = BigInt(acc0After);

            assert.equal(acc0Before, acc0After + transferSize);
            assert.equal(acc1Before + transferSize, acc1After);

        })


        it("Can decrease allowance", async() =>{
            // decreaseAllowance should reduce allowance by a specific amount

            var allowBefore = await token.allowance(accounts[0], accounts[1]);
            allowBefore = BigInt(allowBefore);

            var up = BigInt(1000)
            var down = BigInt(400)

            try{
                await token.approve(accounts[1], up, {from: accounts[0]});
                await token.decreaseAllowance(accounts[1], down, {from: accounts[0]})
            }
            catch(error){}


            var allowAfter = await token.allowance(accounts[0], accounts[1]);
            allowAfter = BigInt(allowAfter);

            assert.equal(allowAfter - allowBefore, up - down);

        })


        it("approve sets allowance properly", async() =>{
            // Makes changes to allowance and then sets it to a specific amount

            var allowBefore = await token.allowance(accounts[0], accounts[1]);
            allowBefore = BigInt(allowBefore);

            var up = BigInt(1000)
            var down = BigInt(400)
            var finalAmount = BigInt(25565)

            try{

                await token.increaseAllowance(accounts[1], up, {from: accounts[0]});
                await token.decreaseAllowance(accounts[1], down, {from: accounts[0]});
                await token.approve(accounts[1], finalAmount, {from: accounts[0]});
            }
            catch(error){}


            var allowAfter = await token.allowance(accounts[0], accounts[1]);
            allowAfter = BigInt(allowAfter);

            assert.equal(allowAfter, finalAmount);

        })

    })

})