import 'package:flutter/material.dart';
import '../widgets/main_drawer.dart';

class SettingsScreen extends StatefulWidget {
  static const routeName = '/settings';

  // final Function saveFilters;
  // final Map<String, bool> currentFilters;

  // SettingsScreen(this.saveFilters, this.currentFilters);

  @override
  _SettingsScreenState createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  // var _glutenFree = false;
  // var _vegan = false;
  // var _vegetarian = false;
  // var _lactoseFree = false;

  @override
  initState() {
    // _glutenFree = widget.currentFilters['gluten'];
    // _lactoseFree = widget.currentFilters['lactose'];
    // _vegetarian = widget.currentFilters['vegetarian'];
    // _vegan = widget.currentFilters['vegan'];
    super.initState();
  }

  Widget _buildSwitchListTile(
    String title,
    String description,
    bool currentValue,
    Function updateValue,
  ) {
    return SwitchListTile(
      title: Text(title),
      value: currentValue,
      subtitle: Text(description),
      // onChanged: updateValue,
      onChanged: null,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Your Filters'),
        actions: [
          IconButton(
            onPressed: () {
              // final selectedFilters = {
              //   'gluten': _glutenFree,
              //   'lactose': _lactoseFree,
              //   'vegan': _vegan,
              //   'vegetarian': _vegetarian,
              // };
              // widget.saveFilters(selectedFilters);
            },
            icon: Icon(
              Icons.save,
            ),
          )
        ],
      ),
      drawer: MainDrawer(),
      body: Column(
        children: [
          Container(
            padding: EdgeInsets.all(20),
            child: Text(
              'Adjust your meal selection',
              style: Theme.of(context).textTheme.title,
            ),
          ),
          Expanded(
            child: ListView(
              children: [
                // _buildSwitchListTile(
                //   'Gluten-free',
                //   'Only include gluten-free meals',
                //   _glutenFree,
                //   (newValue) {
                //     setState(() {
                //       _glutenFree = newValue;
                //     });
                //   },
                // ),
              ],
            ),
          )
        ],
      ),
    );
  }
}
