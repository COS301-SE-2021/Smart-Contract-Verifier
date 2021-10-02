//This will contain any functions that may need to call Metamask from JS


async function signWithMetamask(data, address) {

	var web3 = window.ethereum;
	var res = await web3.request({ method: 'personal_sign' , params: [(data.toString() +' ').trim(), address]});
	return res;
}

async function addNetwork(name, url, id, cName, cSymbol) {

	const pars = [{
		chainId: id,
		chainName: name,
		nativeCurrency: {
			name: cName,
			symbol: cSymbol,
			decimals: 18
		},
		rpcUrls: [url],
//		blockExplorerUrls: ['https://explorer.rsk.co']
	}]

	var netw = await window.ethereum.networkVersion;

	console.log('CurrNet: ' + netw);
	console.log('NewNet: ' + id);
	if (netw != id)
	await window.ethereum.request({ method: 'wallet_addEthereumChain', params: pars });

}

