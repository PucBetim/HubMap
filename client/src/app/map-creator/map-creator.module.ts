import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DragDropModule } from '@angular/cdk/drag-drop';

import { CreationComponent } from './creation/creation.component';
import { CreatorRoutes } from './map-creator.routing';

@NgModule({
  declarations: [
    CreationComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(CreatorRoutes),
    DragDropModule,
  ]
})
export class MapCreatorModule { }
