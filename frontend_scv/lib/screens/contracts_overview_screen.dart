import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/services/Blockchain/tokenService.dart';
import 'package:unison/widgets/funky_text_widget.dart';

import '../models/contracts.dart';
import '../screens/edit_contract_screen.dart';
import '../widgets/app_drawer.dart';
import '../widgets/contracts_grid.dart';

enum FilterOptions {
  Favourites,
  All,
}

class ContractsOverviewScreen extends StatefulWidget {
  @override
  _ContractsOverviewScreenState createState() =>
      _ContractsOverviewScreenState();
}

class _ContractsOverviewScreenState extends State<ContractsOverviewScreen> {
  var _isInit = true;
  var _isLoading = false;

  @override
  void initState() {
    super.initState();
  }

  @override
  void didChangeDependencies() {
    if (_isInit) {
      //running for first time
      setState(() {
        _isLoading = true;
      });
      Provider.of<Contracts>(context).fetchAndSetContracts().then((_) {
        setState(() {
          _isLoading = false;
        });
      });
      _isInit = false;
    }
    super.didChangeDependencies();
  }

  @override
  Widget build(BuildContext context) {
    TokenService tokServ = TokenService();

    return Scaffold(
      appBar: AppBar(
        // title: Text(Global.userAddress),
        title: FunkyText('Agreements Dashboard'),
        actions: [
          FutureBuilder(
              future: tokServ.getAllowance(),
              builder: (context, snap) {
                if (snap.connectionState == ConnectionState.done) {
                  return Container(
                    child: Row(
                      children: [
                        Row(children: [
                          TextButton(
                            child: Text(
                              'Top Up Allowance',
                              style: TextStyle(
                                color: Colors.purple,
                                fontSize: 20,
                              ),
                            ),
                            onPressed: () async {
                              await tokServ
                                  .setAllowance(BigInt.from(10000000000));
                            },
                          ),
                          SizedBox(
                            width: 50,
                          ),
                          Text(
                            'Remaining allowance: ' + snap.data.toString(),
                            style: TextStyle(fontSize: 15),
                          ),
                        ]),
                        SizedBox(width: 30),
                      ],
                    ),
                  );
                }
                return CircularProgressIndicator();
              })
        ],
      ),
      drawer: AppDrawer(),
      body: _isLoading
          ? Center(
              child: AwesomeLoader(
                loaderType: AwesomeLoader.AwesomeLoader4,
                color: Color.fromRGBO(50, 183, 196, 0.5),
              ),
            )
          // : ContractsGrid(_showOnlyFavorites),
          : ContractsGrid(),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
      floatingActionButton: Padding(
        padding: const EdgeInsets.only(bottom: 50),
        child: FloatingActionButton.extended(
          label: Text('Create New Agreement'),
          icon: Icon(Icons.add),
          onPressed: () =>
              Navigator.of(context).pushNamed(EditContractScreen.routeName),
          backgroundColor: Color.fromRGBO(182, 80, 158, 1),
        ),
      ),
    );
  }
}
