// spring-message-board-demo
// Refer to LICENCE.txt for licence details.
import 'package:frontend/app/inject/ViewModelModule.dart';
import 'package:frontend/dataSrc/SimpleTextDataSource.dart';
import 'package:frontend/repository/SimpleTextRepository.dart';

import 'DataSourceModule.dart';
import '_SingletonHolder.dart';

/// Repository definitions for [ViewModelModule].
///
/// Repositories are responsible to mediate data from various sources and provide them to View models.
///
/// Author: Francesco Jo(<nimbusob@gmail.com>) <br/>
/// Since: 07 - Sep - 2020
class RepositoryModule extends SingletonHolder {
  final DataSourceModule _dataSourceModule;

  RepositoryModule(this._dataSourceModule);

  ISimpleTextRepository simpleTextRepository() {
    final ISimpleTextDataSource dataSrc = _dataSourceModule.simpleTextDataSource();

    return super.getInstanceOf(() => ISimpleTextRepository.newInstance(dataSrc));
  }
}
