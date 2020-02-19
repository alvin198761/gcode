import app from './app';
import settings from "./settings";
import tagsView from "./tagsView";
import permission from "./permission";
import Vue from 'vue';
import Vuex from 'vuex'

Vue.use( Vuex );
export default new Vuex.Store({
    modules: {
        app: app,
        settings: settings,
        tagsView: tagsView,
        permission: permission
    }
})
