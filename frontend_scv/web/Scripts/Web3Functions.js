//This will contain any functions that may need to call Metamask from JS


async function signWithMetamask(data, address) {

	console.log('Signing ' + data);
	var web3 = window.ethereum;
	var res = await web3.request({ method: 'personal_sign' , params: [(data.toString() +' ').trim(), address]});
	console.log('Signed: ' + res);
	return res;
}
