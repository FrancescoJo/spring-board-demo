// spring-message-board-demo
// Refer to LICENCE.txt for licence details.
import 'package:frontend/app/inject/RepositoryModule.dart';
import 'package:frontend/dataSrc/SimpleTextDataSource.dart';

import '_SingletonHolder.dart';

/// Data source definitions for [RepositoryModule].
///
/// Data sources are responsible to retrieve data from its defined locations and provide them to Repositories.
///
/// Author: Francesco Jo(<nimbusob@gmail.com>) <br/>
/// Since: 07 - Sep - 2020
class DataSourceModule extends SingletonHolder {
  ISimpleTextDataSource simpleTextDataSource() {
    return super.getInstanceOf(() => ISimpleTextDataSource.newInstance());
  }
}
