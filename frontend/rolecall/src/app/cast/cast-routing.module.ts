import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CastingEditor } from './casting_editor.component';


const routes: Routes = [
  { path: 'cast/:uuid', component: CastingEditor },
  { path: 'cast', component: CastingEditor }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CastRoutingModule { }