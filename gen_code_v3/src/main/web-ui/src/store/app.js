import Cookies from 'js-cookie'

export default {
    state: {
        sidebar: {
            opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
            withoutAnimation: false
        },
        device: 'desktop',
        size: Cookies.get('size') || 'medium'
    },
    getters: {},
    mutations: {
        ["TOGGLE_SIDEBAR"]: state => {
            debugger
            state.sidebar.opened = !state.sidebar.opened
            state.sidebar.withoutAnimation = false
            if (state.sidebar.opened) {
                Cookies.set('sidebarStatus', 1)
            } else {
                Cookies.set('sidebarStatus', 0)
            }
        },
        ["CLOSE_SIDEBAR"]: (state, withoutAnimation) => {
            Cookies.set('sidebarStatus', 0)
            state.sidebar.opened = false
            state.sidebar.withoutAnimation = withoutAnimation
        },
        ["TOGGLE_DEVICE"]: (state, device) => {
            state.device = device
        },
        ["SET_SIZE"]: (state, size) => {
            state.size = size
            Cookies.set('size', size)
        }
    },
    actions: {
        ["app/toggleSideBar"]({commit}) {
            commit('TOGGLE_SIDEBAR')
        },
        ["app/closeSideBar"]({commit}, {withoutAnimation}) {
            commit('CLOSE_SIDEBAR', withoutAnimation)
        },
        ["app/toggleDevice"]({commit}, device) {
            commit('TOGGLE_DEVICE', device)
        },
        ["app/setSize"]({commit}, size) {
            commit('SET_SIZE', size)
        }
    }
};
