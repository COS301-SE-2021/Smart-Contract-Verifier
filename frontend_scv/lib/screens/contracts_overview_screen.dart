import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/providers/global.dart';

import '../providers/contracts.dart';
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
  var _showOnlyFavorites = false;
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
    return Scaffold(
      appBar: AppBar(
        title: Text(Global.userAddress),
        // title: Text('Dashboard'),
        actions: <Widget>[
          PopupMenuButton(
            onSelected: (FilterOptions selectedValue) {
              setState(() {
                if (selectedValue == FilterOptions.Favourites) {
                  _showOnlyFavorites = true;
                } else {
                  _showOnlyFavorites = false;
                }
              });
            },
            icon: Icon(
              Icons.more_vert,
            ),
            itemBuilder: (_) => [
              PopupMenuItem(
                child: Text('Only Favourites'),
                value: FilterOptions.Favourites,
              ),
              PopupMenuItem(
                child: Text('Show All'),
                value: FilterOptions.All,
              ),
            ],
          ),
          //TODO: Add notification button with badge
        ],
      ),
      drawer: AppDrawer(),
      body: _isLoading
          ? Center(
              child: CircularProgressIndicator(),
            )
          // : ContractsGrid(_showOnlyFavorites),
          : ContractsGrid(),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: () =>
            Navigator.of(context).pushNamed(EditContractScreen.routeName),
      ),
    );
  }
}
