// spring-message-board-demo
// Refer to LICENCE.txt for licence details.
import 'package:frontend/app/inject/RepositoryModule.dart';
import 'package:frontend/app/ui/MyAppViewModel.dart';
import 'package:frontend/repository/SimpleTextRepository.dart';

import '_SingletonHolder.dart';

/// View Model definitions for application.
///
/// View models are responsible to provide data to the views.
///
/// Author: Francesco Jo(<nimbusob@gmail.com>) <br/>
/// Since: 07 - Sep - 2020
class ViewModelModule extends SingletonHolder {
  final RepositoryModule _repositoryModule;

  ViewModelModule(this._repositoryModule);

  MyAppViewModel myAppViewModel() {
    final ISimpleTextRepository repository = _repositoryModule.simpleTextRepository();

    // TODO: Link this instance to Widget providers
    return MyAppViewModel(repository);
  }
}
