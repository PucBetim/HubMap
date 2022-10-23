import { CreationComponent } from './main/creation.component';
import { Routes } from '@angular/router';
import { PendingChangesGuard } from '../core/services/guard.service';


export const CreatorRoutes: Routes = [
  { path: '', component: CreationComponent, canDeactivate: [PendingChangesGuard] },
];
