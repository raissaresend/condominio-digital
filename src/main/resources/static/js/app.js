class AppCondominio {

    static CONFIG = {
        seletores: {
            money: ".componentemoney",
            data: ".componentedata",
            dataManual: ".componentedatamanual",
            hora: ".componentehora",
            cpf: ".componentecpf",
            confirm: "[hx-confirm]:not([data-confirm-ready])",
            buscaContainer: ".searchable-select-container:not([data-initialized])",
            senha: ".toggle-senha",
        },
        maskMoney: {
            prefix: "",
            suffix: "",
            fixed: true,
            fractionDigits: 2,
            decimalSeparator: ",",
            thousandsSeparator: ".",
            autoCompleteDecimal: true,
        },
        datePicker: {
            dateFormat: "dd/mm/yyyy",
            customWeekDays: ["Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"],
            customMonths: ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
            customClearBTN: "Limpar",
            customCancelBTN: "Cancelar",
            theme: { theme_color: "rgb(31 41 55)" },
        },
        texto: {
            cancelar: "Cancelar",
            remover: "Remover",
            tituloConfirm: "Você tem certeza?",
        }
    };

    /**
     * Instância global do SweetAlert2 para notificações (Toasts).
     */
    static Toast = Swal.mixin({
        toast: true,
        position: "center-end",
        iconColor: "white",
        customClass: { popup: "colored-toast" },
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        grow: "row",
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        },
    });

    /**
     * Armazena os tokens CSRF para requisições assíncronas (HTMX).
     */
    static csrfData = {
        header: null,
        token: null
    };

    /**
     * Método principal de inicialização da classe.
     */
    static init() {
        this.lerCsrfDoHTML();
        this.registrarListenersGlobais();
    }

    /**
     * Varre o DOM em busca de componentes customizados e aplica suas respectivas lógicas/bibliotecas.
     */
    static atualizarComponentes(container = document) {
        this.prepararMoney(container);
        this.prepararData(container);
        this.prepararHora(container);
        this.prepararConfirmacoes(container);
        this.prepararBusca(container);
        this.prepararCpf(container);
        this.prepararSenha(container);
        this.prepararNumero(container);
    }

    /**
     * Extrai os tokens CSRF contidos nas meta tags da página gerada pelo Spring Security.
     */
    static lerCsrfDoHTML() {
        const tokenMeta = document.querySelector('meta[name="_csrf"]');
        const headerMeta = document.querySelector('meta[name="_csrf_header"]');
        if (tokenMeta && headerMeta) {
            this.csrfData.token = tokenMeta.content;
            this.csrfData.header = headerMeta.content;
        }
    }

    /**
     * Registra os ouvintes globais para lidar com requisições HTMX e eventos customizados.
     */
    static registrarListenersGlobais() {
        if (typeof htmx !== 'undefined') {
            htmx.onLoad((elt) => {
                this.atualizarComponentes(elt);
            });

            htmx.on("htmx:afterSettle", (evt) => {
                if (evt.detail.xhr) {
                    let header = "X-CSRF-TOKEN";
                    let token = evt.detail.xhr.getResponseHeader(header);
                    if (token == null) {
                        header = "X-XSRF-TOKEN";
                        token = evt.detail.xhr.getResponseHeader(header);
                    }
                    if (token) {
                        this.csrfData.header = header;
                        this.csrfData.token = token;
                    }
                }
            });
        }

        document.body.addEventListener("htmx:configRequest", (evt) => {
            evt.detail.headers["accept"] = "text/html-partial";
            if (evt.detail.verb !== "get") {
                if (this.csrfData.header != null && this.csrfData.token != null) {
                    evt.detail.headers[this.csrfData.header] = this.csrfData.token;
                }
            }
        });

        document.body.addEventListener("exibirAlerta", (evt) => {
            this.mostrarNotificacaoViaHeader(evt.detail);
        });

        if (!document.body.dataset.clickOutsideRegistered) {
            document.addEventListener("click", (event) => {
                const activeDropdown = document.querySelector(".custom-select-dropdown:not(.hidden)");
                if (activeDropdown && !event.target.closest(".searchable-select-container")) {
                    activeDropdown.classList.add("hidden");
                    const activeTrigger = activeDropdown.closest(".searchable-select-container").querySelector(".custom-select-trigger");
                    if (activeTrigger) {
                        this._removeFocus(activeTrigger);
                    }
                }
            });
            document.body.dataset.clickOutsideRegistered = "true";
        }
    }

    /**
     * Aplica a máscara monetária aos inputs designados.
     */
    static prepararMoney(container = document) {
        let inputs = container.querySelectorAll(this.CONFIG.seletores.money);
        if (container.matches && container.matches(this.CONFIG.seletores.money)) {
            inputs = [container];
        }

        inputs.forEach((input) => {
            if (typeof SimpleMaskMoney !== 'undefined') {
                SimpleMaskMoney.setMask(input, this.CONFIG.maskMoney);
            }
            input.classList.remove("componentemoney");
        });
    }

    /**
     * Aplica a máscara e o calendário visual (MCDatepicker) aos campos de data.
     */
    static prepararData(container = document) {
        let inputs = container.querySelectorAll(this.CONFIG.seletores.data);
        if (container.matches && container.matches(this.CONFIG.seletores.data)) {
            inputs = [container];
        }

        inputs.forEach((input) => {
            let datePicker = null;

            const sincronizarCalendario = () => {
                if (datePicker && input.value.length === 10) {
                    const parts = input.value.split("/");
                    const d = new Date(+parts[2], parts[1] - 1, +parts[0]);
                    if (!isNaN(d.getTime())) {
                        datePicker.setFullDate(d);
                    }
                }
            };

            let apagando = false;
            input.addEventListener("keydown", (e) => { apagando = e.key === "Backspace"; });

            input.addEventListener("input", (e) => {
                if (apagando) return;
                let v = e.target.value.replace(/\D/g, "");
                if (v.length > 2 && v.length <= 4) {
                    v = v.substring(0, 2) + "/" + v.substring(2);
                } else if (v.length > 4) {
                    v = v.substring(0, 2) + "/" + v.substring(2, 4) + "/" + v.substring(4, 8);
                }
                e.target.value = v;
                sincronizarCalendario();
            });

            input.addEventListener("blur", sincronizarCalendario);

            input.classList.remove("componentedata");
            input.setAttribute("placeholder", "DD/MM/AAAA");
            input.setAttribute("maxlength", "10");

            if (typeof MCDatepicker !== 'undefined') {
                const btnId = "btn-" + input.id;
                const btn = (container.querySelector ? container.querySelector("#" + btnId) : null)
                    || document.getElementById(btnId);

                if (btn) {
                    const anchorId = "anchor-" + input.id;
                    let anchor = document.getElementById(anchorId);

                    if (!anchor) {
                        anchor = document.createElement("span");
                        anchor.id = anchorId;
                        anchor.style.cssText = "position: absolute; width: 1px; height: 1px; opacity: 0; pointer-events: none;";
                        btn.appendChild(anchor);
                    }

                    datePicker = MCDatepicker.create({
                        ...this.CONFIG.datePicker,
                        el: "#" + anchor.id,
                        autoClose: false
                    });

                    btn.addEventListener("click", (e) => {
                        e.preventDefault();
                        datePicker.open();
                    });

                    btn.addEventListener("keydown", (e) => {
                        if (e.key === " " || e.key === "Enter") {
                            e.preventDefault();
                            datePicker.open();
                        }
                    });

                    datePicker.onSelect((date, formatedDate) => {
                        input.value = formatedDate;
                        input.dispatchEvent(new Event('input', { bubbles: true }));
                    });

                    sincronizarCalendario();
                }
            }
        });
    }

    /**
     * Aplica a máscara e o seletor visual de horas (mdDateTimePicker) aos campos de horário.
     */
    static prepararHora(container = document) {
        let inputs = container.querySelectorAll(this.CONFIG.seletores.hora);
        if (container.matches && container.matches(this.CONFIG.seletores.hora)) {
            inputs = [container];
        }

        inputs.forEach((input) => {
            let apagando = false;

            input.addEventListener("keydown", (e) => {
                apagando = e.key === "Backspace";
            });

            input.addEventListener("input", (e) => {
                if (apagando) return;
                let v = e.target.value.replace(/\D/g, "");
                if (v.length > 2) {
                    v = v.substring(0, 2) + ":" + v.substring(2, 4);
                }
                e.target.value = v;
            });

            input.classList.remove("componentehora");
            input.setAttribute("placeholder", "HH:MM");
            input.setAttribute("maxlength", "5");

            if (typeof mdDateTimePicker !== 'undefined') {
                const btnId = "btn-" + input.id;
                const btn = (container.querySelector ? container.querySelector("#" + btnId) : null)
                    || document.getElementById(btnId);

                const dialog = new mdDateTimePicker.default({
                    type: "time", mode: true, inner24: true, cancel: this.CONFIG.texto.cancelar,
                });

                const sincronizarRelogio = () => {
                    if (input.value && input.value.length === 5) {
                        const partes = input.value.split(':');
                        const h = parseInt(partes[0], 10);
                        const m = parseInt(partes[1], 10);

                        if (h >= 0 && h <= 23 && m >= 0 && m <= 59) {
                            if (typeof moment !== 'undefined') {
                                dialog.time = moment(input.value, "HH:mm");
                            }
                        }
                    }
                };

                if (btn) {
                    btn.addEventListener("click", (e) => {
                        e.preventDefault();
                        sincronizarRelogio();
                        dialog.toggle();
                    });

                    btn.addEventListener("keydown", (e) => {
                        if (e.key === " " || e.key === "Enter") {
                            e.preventDefault();
                            sincronizarRelogio();
                            dialog.toggle();
                        }
                    });
                }

                dialog.trigger = input;

                input.addEventListener("onOk", () => {
                    input.value = dialog.time.format("HH:mm");
                    input.dispatchEvent(new Event('input', { bubbles: true }));
                });
            }
        });
    }

    /**
     * Aplica a formatação em tempo real de CPF.
     */
    static prepararCpf(container = document) {
        let inputs = container.querySelectorAll(this.CONFIG.seletores.cpf);
        if (container.matches && container.matches(this.CONFIG.seletores.cpf)) {
            inputs = [container];
        }

        inputs.forEach((input) => {
            let apagando = false;

            input.addEventListener("keydown", (e) => {
                apagando = e.key === "Backspace";
            });

            input.addEventListener("input", (e) => {
                if (apagando) return;

                let v = e.target.value.replace(/\D/g, "");

                if (v.length > 9) {
                    v = v.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, "$1.$2.$3-$4");
                } else if (v.length > 6) {
                    v = v.replace(/(\d{3})(\d{3})(\d{1,3})/, "$1.$2.$3");
                } else if (v.length > 3) {
                    v = v.replace(/(\d{3})(\d{1,3})/, "$1.$2");
                }

                e.target.value = v;
            });

            input.classList.remove("componentecpf");
            input.setAttribute("placeholder", "000.000.000-00");
            input.setAttribute("maxlength", "14");
        });
    }

    /**
     * Alterna a visibilidade (toggle text/password) de inputs de senha.
     */
    static prepararSenha(container = document) {
        let botoes = container.querySelectorAll(this.CONFIG.seletores.senha);
        if (container.matches && container.matches(this.CONFIG.seletores.senha)) {
            botoes = [container];
        }

        botoes.forEach(btn => {
            btn.addEventListener("click", (e) => {
                e.preventDefault();
                const containerRelativo = btn.closest(".relative");
                const input = containerRelativo.querySelector("input:not([type='hidden'])");
                const olhoAberto = btn.querySelector(".icone-olho-aberto");
                const olhoFechado = btn.querySelector(".icone-olho-fechado");

                if (input.type === "password") {
                    input.type = "text";
                    olhoFechado.classList.add("hidden");
                    olhoAberto.classList.remove("hidden");
                } else {
                    input.type = "password";
                    olhoAberto.classList.add("hidden");
                    olhoFechado.classList.remove("hidden");
                }
            });
        });
    }

    /**
     * Formata inputs numéricos genéricos.
     */
    static prepararNumero(container = document) {
        const inputs = container.querySelectorAll(".input-numero");

        inputs.forEach(input => {
            const isDecimal = input.dataset.decimal === "true";
            const showGroups = input.dataset.groups === "true";

            const formatar = (valor) => {
                if (!valor) return "";
                let v = valor.toString();

                if (v.includes(',')) {
                    v = v.replace(/\./g, "");
                } else if (isDecimal && v.includes('.')) {
                    v = v.replace('.', ',');
                }

                v = v.replace(/[^0-9,]/g, "");

                if (showGroups) {
                    let partes = v.split(',');
                    partes[0] = partes[0].replace(/\B(?=(\d{3})+(?!\d))/g, ".");
                    v = partes.join(',');
                }
                return v;
            };

            if (input.value) {
                input.value = formatar(input.value);
            }

            input.addEventListener("input", (e) => {
                let v = e.target.value.replace(/[^0-9,]/g, ""); 
                if ((v.match(/,/g) || []).length > 1) {
                    v = v.substring(0, v.lastIndexOf(','));
                }
                if (!isDecimal) {
                    v = v.replace(/,/g, "");
                }
                if (showGroups) {
                    let partes = v.split(',');
                    partes[0] = partes[0].replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ".");
                    v = partes.join(',');
                }
                e.target.value = v;
            });
        });
    }

    /**
     * Configura a interceptação para modais de confirmação via SweetAlert.
     */
    static prepararConfirmacoes(container = document) {
        let elementos = container.querySelectorAll(this.CONFIG.seletores.confirm);
        if (container.matches && container.matches(this.CONFIG.seletores.confirm)) {
            elementos = [container];
        }

        elementos.forEach((elemento) => {
            elemento.addEventListener("htmx:confirm", (e) => this._dispararSweetAlert(e));
            elemento.setAttribute("data-confirm-ready", "true");
        });
    }

    static _dispararSweetAlert(e) {
        e.preventDefault();
        Swal.fire({
            title: AppCondominio.CONFIG.texto.tituloConfirm,
            text: e.detail.question,
            icon: "warning",
            showCancelButton: true,
            cancelButtonText: AppCondominio.CONFIG.texto.cancelar,
            confirmButtonText: AppCondominio.CONFIG.texto.remover,
            confirmButtonColor: "#3085d6",
        }).then((result) => {
            if (result.isConfirmed) {
                e.detail.issueRequest(true);
            }
        });
    }

    static mostrarNotificacaoViaHeader(dados) {
        if (dados) {
            this.Toast.fire({ icon: dados.tipo, title: dados.mensagem, timer: dados.intervalo || 3000 });
        }
    }

    static _applyFocus(trigger) {
        trigger.classList.remove("border-gray-300");
        trigger.classList.add("ring-1", "border-gray-500", "ring-gray-500");
    }

    static _removeFocus(trigger) {
        trigger.classList.remove("ring-1", "border-gray-500", "ring-gray-500");
        trigger.classList.add("border-gray-300");
    }

    /**
     * Inicializa lógica de Selects pesquisáveis customizados.
     */
    static prepararBusca(container = document) {
        let containers = container.querySelectorAll(this.CONFIG.seletores.buscaContainer);
        if (container.matches && container.matches(this.CONFIG.seletores.buscaContainer)) {
            containers = [container];
        }

        containers.forEach((container) => {
            container.dataset.initialized = "true";
            const trigger = container.querySelector(".custom-select-trigger");
            const dropdown = container.querySelector(".custom-select-dropdown");
            const optionsList = container.querySelector(".options-list");
            const selectedValueSpan = container.querySelector(".selected-value-span");
            const hiddenInput = container.querySelector(".hidden-select-input");

            if (trigger && dropdown) {
                trigger.addEventListener("click", (e) => {
                    const isCurrentlyOpen = !dropdown.classList.contains("hidden");
                    document.querySelectorAll(".custom-select-dropdown:not(.hidden)").forEach(d => {
                        if (d !== dropdown) {
                            d.classList.add("hidden");
                            const otherTrigger = d.closest(".searchable-select-container")?.querySelector(".custom-select-trigger");
                            if (otherTrigger) {
                                this._removeFocus(otherTrigger);
                            }
                        }
                    });
                    dropdown.classList.toggle("hidden");
                    if (!isCurrentlyOpen) {
                        this._applyFocus(trigger);
                    } else {
                        this._removeFocus(trigger);
                    }
                    e.stopPropagation();
                    if (!isCurrentlyOpen) {
                        const searchInput = dropdown.querySelector('input[type="text"]');
                        if (searchInput) {
                            searchInput.focus();
                        }
                    }
                });
            }

            if (optionsList) {
                optionsList.addEventListener("click", (event) => {
                    const option = event.target.closest("li");
                    if (option) {
                        if (selectedValueSpan) {
                            selectedValueSpan.textContent = option.dataset.text || option.textContent.trim();
                        }
                        if (hiddenInput) hiddenInput.value = option.dataset.value;
                        if (dropdown) dropdown.classList.add("hidden");
                        const trigger = container.querySelector(".custom-select-trigger");
                        if (trigger) {
                            AppCondominio._removeFocus(trigger);
                        }
                    }
                });
            }
        });
    }
}

document.addEventListener("DOMContentLoaded", () => AppCondominio.init());
