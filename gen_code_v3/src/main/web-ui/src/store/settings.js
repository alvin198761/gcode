import variables from '@/styles/element-variables.scss'
import defaultSettings from '@/settings'

const { showSettings, tagsView, fixedHeader, sidebarLogo } = defaultSettings

export default {
    state: {
        theme: variables.theme,
        showSettings: showSettings,
        tagsView: tagsView,
        fixedHeader: fixedHeader,
        sidebarLogo: sidebarLogo
    },
    getters: {},
    mutations: {
        ["settings/CHANGE_SETTING"]: (state, { key, value }) => {
            if (state.hasOwnProperty(key)) {
                state[key] = value
            }
        }
    },
    actions: {
        ["settings/changeSetting"]({ commit }, data) {
            commit('settings/CHANGE_SETTING', data)
        }
    }
};
